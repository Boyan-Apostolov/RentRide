package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingsForUserUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBookingsForUserUseCaseImpl implements GetBookingsForUserUseCase {
    private final BookingRepository bookingRepository;
    private final AccessToken accessToken;

    @Value("${DEFAULT_PAGE_SIZE}")
    private int DefaultPageSize;

    @Override
    public List<Booking> getBookingsForUser() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        return this.bookingRepository
                .findByUserId(accessToken.getUserId(), pageable)
                .stream().map(BookingConverter::convert)
                .toList();
    }

    @Override
    public Long getCount() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        return this.bookingRepository
                .findByUserId(accessToken.getUserId(), pageable)
                .stream().count();
    }

    @Override
    public List<Booking> getBookingsForUser(int page) {
        Pageable pageable = PageRequest.of(page, DefaultPageSize);

        return this.bookingRepository
                .findByUserId(accessToken.getUserId(), pageable)
                .stream().map(BookingConverter::convert)
                .toList();
    }
}
