package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.booking.GetBookingByIdUseCase;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetBookingByIdUseCaseImpl implements GetBookingByIdUseCase {
    private BookingRepository bookingRepository;

    @Override
    public Booking getBookingById(Long bookingId) {
        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElse(null);
        return BookingConverter.convert(bookingEntity);
    }
}
