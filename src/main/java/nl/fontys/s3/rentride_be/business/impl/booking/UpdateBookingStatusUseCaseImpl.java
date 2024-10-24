package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.booking.GetBookingsForCarUseCase;
import nl.fontys.s3.rentride_be.business.useCases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.business.useCases.car.MoveCarUseCase;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateBookingStatusUseCaseImpl implements UpdateBookingStatusUseCase {
    private BookingRepository bookingRepository;
    private GetBookingsForCarUseCase getBookingsForCarUseCase;
    private MoveCarUseCase moveCarUseCase;

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

            System.out.printf("UpdateStatus->For car id: %s, Status: %s%n",
                    bookingEntity.getCar().getId(),
                    newStatus.name());

            bookingEntity.setStatus(newStatus);
            bookingRepository.save(bookingEntity);

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

    private void tryMoveCarAtStart(BookingEntity bookingEntity) {
        CarEntity car = bookingEntity.getCar();

        if(!car.getCity().getId().equals(bookingEntity.getStartCity().getId())){
            System.out.printf("Booking started with missing car->Moving car id: %s, To City Id: %s",
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
            System.out.printf("Booking finished->Moving car id: %s, To City Id: %s",
                    bookingEntity.getCar().getId(),
                    possibleBooking.get().getStartCity().getId());

            moveCarUseCase.moveCar(car.getId(),
                    possibleBooking.get().getStartCity().getId()
            );
        }
    }
}
