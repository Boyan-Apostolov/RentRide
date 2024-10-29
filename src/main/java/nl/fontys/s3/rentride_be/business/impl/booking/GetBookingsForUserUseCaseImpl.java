package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingsForUserUseCase;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetBookingsForUserUseCaseImpl implements GetBookingsForUserUseCase {
    private BookingRepository bookingRepository;

    @Override
    public List<Booking> getBookingsForUser(Long userId) {
        return this.bookingRepository
                .findByUserId(userId)
                .stream().map(BookingConverter::convert)
                .toList();
    }
}
