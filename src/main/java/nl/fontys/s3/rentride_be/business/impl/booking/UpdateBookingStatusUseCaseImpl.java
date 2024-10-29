package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingsForCarUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.car.MoveCarUseCase;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateBookingStatusUseCaseImpl implements UpdateBookingStatusUseCase {
    private BookingRepository bookingRepository;
    private GetBookingsForCarUseCase getBookingsForCarUseCase;
    private MoveCarUseCase moveCarUseCase;

    private static final Logger logger = LoggerFactory.getLogger(UpdateBookingStatusUseCaseImpl.class);

    @Override
    public void updateBookingStatus(Long bookingId, BookingStatus newStatus) {
        Optional<BookingEntity> bookingEntityOptional = bookingRepository.findById(bookingId);

        if (bookingEntityOptional.isEmpty()) {
            throw new NotFoundException("UpdateStatus->Booking");
        }

        BookingEntity bookingEntity = bookingEntityOptional.get();

        //Allowed status changes : Unpaid -> Paid/ Unpaid -> Canceled/ Paid -> Active/  Active -> Finished
        if ((newStatus == BookingStatus.Paid && bookingEntity.getStatus() == BookingStatus.Unpaid)
                || (newStatus == BookingStatus.Canceled && bookingEntity.getStatus() == BookingStatus.Unpaid)
                || (newStatus == BookingStatus.Active && bookingEntity.getStatus() == BookingStatus.Paid)
                || (newStatus == BookingStatus.Finished && bookingEntity.getStatus() == BookingStatus.Active)) {

            logger.info("Update Status->For car id: {}, Status: {}",
                    bookingEntity.getCar().getId(),
                    newStatus.name());

            bookingEntity.setStatus(newStatus);
            bookingRepository.save(bookingEntity);

            if (bookingEntity.getStatus() == BookingStatus.Canceled) {
                refundAndStopSchedule(bookingEntity);
            }


            if (bookingEntity.getStatus() == BookingStatus.Active) {
                tryMoveCarAtStart(bookingEntity);
            }

            if (bookingEntity.getStatus() == BookingStatus.Finished) {
                tryMoveCarAtEnd(bookingEntity);
            }
        } else {
            throw new NotImplementedException("Invalid new status for Booking " + bookingEntity.getStatus() + "> " + newStatus);
        }
    }

    private void refundAndStopSchedule(BookingEntity bookingEntity){
        if(!bookingEntity.getPaymentId().isEmpty()){
            //TODO: Refund stripe payment
        }

        //TODO: Cancel scheduled jobs
    }

    private void tryMoveCarAtStart(BookingEntity bookingEntity) {
        CarEntity car = bookingEntity.getCar();

        if(!car.getCity().getId().equals(bookingEntity.getStartCity().getId())){
            logger.info("Booking started with missing car -> Moving car id: {}, To City Id: {}",
                    bookingEntity.getCar().getId(),
                    bookingEntity.getStartCity().getId());

            moveCarUseCase.moveCar(car.getId(),
                    bookingEntity.getStartCity().getId()
            );
        }
    }

    private void tryMoveCarAtEnd(BookingEntity bookingEntity) {
        CarEntity car = bookingEntity.getCar();

        List<Booking> carBookings = getBookingsForCarUseCase.getBookings(car.getId());

        Optional<Booking> possibleBooking = carBookings.stream()
                .filter(booking -> booking.getStartDateTime().isAfter(bookingEntity.getEndDateTime()))
                .findFirst();

        if(!car.getCity().getId().equals(bookingEntity.getStartCity().getId()) && possibleBooking.isPresent()){
            logger.info("Booking finished->Moving car id: {}, To City Id: {}",
                    bookingEntity.getCar().getId(),
                    possibleBooking.get().getStartCity().getId());

            moveCarUseCase.moveCar(car.getId(),
                    possibleBooking.get().getStartCity().getId()
            );
        }
    }
}
