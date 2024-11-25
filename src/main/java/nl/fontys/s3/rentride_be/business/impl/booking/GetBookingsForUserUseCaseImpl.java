package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingsForUserUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetBookingsForUserUseCaseImpl implements GetBookingsForUserUseCase {
    private BookingRepository bookingRepository;
    private AccessToken accessToken;

    @Override
    public List<Booking> getBookingsForUser() {
        return this.bookingRepository
                .findByUserId(accessToken.getUserId())
                .stream().map(BookingConverter::convert)
                .toList();
    }
}
