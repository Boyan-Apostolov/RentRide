package nl.fontys.s3.rentride_be.business.useCases.booking;

import nl.fontys.s3.rentride_be.domain.booking.Booking;

public interface GetBookingByIdUseCase {
    Booking getBookingById(Long bookingId);
}
