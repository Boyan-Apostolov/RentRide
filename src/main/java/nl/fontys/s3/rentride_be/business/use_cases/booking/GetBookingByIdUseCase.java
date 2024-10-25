package nl.fontys.s3.rentride_be.business.use_cases.booking;

import nl.fontys.s3.rentride_be.domain.booking.Booking;

public interface GetBookingByIdUseCase {
    Booking getBookingById(Long bookingId);
}
