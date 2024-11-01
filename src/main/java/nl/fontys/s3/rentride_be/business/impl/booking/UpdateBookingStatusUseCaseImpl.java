package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingsForCarUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.ScheduleBookingJobsUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.car.MoveCarUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.payment.RefundPaymentUseCase;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
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
    private ScheduleBookingJobsUseCase scheduleBookingJobsUseCase;
    private RefundPaymentUseCase refundPaymentUseCase;

    private static final Logger logger = LoggerFactory.getLogger(UpdateBookingStatusUseCaseImpl.class);

    @Override
    public void updateBookingStatus(Long bookingId, BookingStatus newStatus) {
        BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("UpdateStatus->Booking"));

        if (isStatusTransitionValid(bookingEntity.getStatus(), newStatus)) {
            logger.info("Update Status->For car id: {}, Status: {}",
                    bookingEntity.getCar().getId(), newStatus.name());

            bookingEntity.setStatus(newStatus);
            bookingRepository.save(bookingEntity);
            handlePostStatusUpdateActions(bookingEntity);
        }
    }

    private boolean isStatusTransitionValid(BookingStatus currentStatus, BookingStatus newStatus) {
        return (newStatus == BookingStatus.Paid && currentStatus == BookingStatus.Unpaid)
                || (newStatus == BookingStatus.Canceled && currentStatus == BookingStatus.Unpaid)
                || (newStatus == BookingStatus.Canceled && currentStatus == BookingStatus.Paid)
                || (newStatus == BookingStatus.Active && currentStatus == BookingStatus.Paid)
                || (newStatus == BookingStatus.Finished && currentStatus == BookingStatus.Active);
    }

    private void handlePostStatusUpdateActions(BookingEntity bookingEntity) {
        switch (bookingEntity.getStatus()) {
            case Canceled -> refundAndStopSchedule(bookingEntity);
            case Active -> tryMoveCarAtStart(bookingEntity);
            case Finished -> tryMoveCarAtEnd(bookingEntity);
            default -> {
                // No action required for other statuses
            }
        }
    }

    @Override
    public void setBookingPaymentId(Long bookingId, String paymentId) {
        Optional<BookingEntity> bookingEntityOptional = bookingRepository.findById(bookingId);

        if (bookingEntityOptional.isEmpty()) {
            throw new NotFoundException("UpdateStatus->Booking");
        }

        BookingEntity bookingEntity = bookingEntityOptional.get();
        bookingEntity.setPaymentId(paymentId);
        bookingRepository.save(bookingEntity);
    }

    private void refundAndStopSchedule(BookingEntity bookingEntity){
        if(!bookingEntity.getPaymentId().isEmpty()){
            refundPaymentUseCase.refundPayment(bookingEntity.getPaymentId());
        }

        logger.info("Canceling schedules for booking with id: {}", bookingEntity.getId());
        scheduleBookingJobsUseCase.cancelJobsByBookingId(bookingEntity.getId());
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
