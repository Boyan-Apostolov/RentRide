package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingsUseCase;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetBookingsUseCaseImpl implements GetBookingsUseCase {
    private BookingRepository bookingRepository;

    @Override
    public List<Booking> getBookings() {
        return this.bookingRepository
                .findAll()
                .stream()
                .map(BookingConverter::convert)
                .toList();
    }
}
