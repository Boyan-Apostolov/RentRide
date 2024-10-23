package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateBookingStatusUseCaseImpl implements UpdateBookingStatusUseCase {
    private BookingRepository bookingRepository;

    @Override
    public void updateBookingStatus(Long bookingId, BookingStatus newStatus) {
        Optional<BookingEntity> bookingEntityOptional = bookingRepository.findById( bookingId);

        if(bookingEntityOptional.isEmpty()) {
            throw new NotFoundException("UpdateStatus->Booking");
        }

        BookingEntity bookingEntity = bookingEntityOptional.get();

        //Allowed status changes : Unpaid -> Paid/ Unpaid -> Canceled/ Paid -> Finished
        if((newStatus == BookingStatus.Paid  && bookingEntity.getStatus() == BookingStatus.Unpaid)
        || (newStatus == BookingStatus.Canceled && bookingEntity.getStatus() == BookingStatus.Unpaid)
        || (newStatus == BookingStatus.Finished && bookingEntity.getStatus() == BookingStatus.Paid)){
            bookingEntity.setStatus(newStatus);

            bookingRepository.save(bookingEntity);
        }else{
            throw new NotImplementedException("Invalid new status for Booking");
        }
    }
}
