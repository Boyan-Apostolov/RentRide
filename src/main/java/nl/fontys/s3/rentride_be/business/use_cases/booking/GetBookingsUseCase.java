package nl.fontys.s3.rentride_be.business.use_cases.booking;

import nl.fontys.s3.rentride_be.domain.booking.Booking;

import java.util.List;

public interface GetBookingsUseCase {
    List<Booking> getBookings();
}
