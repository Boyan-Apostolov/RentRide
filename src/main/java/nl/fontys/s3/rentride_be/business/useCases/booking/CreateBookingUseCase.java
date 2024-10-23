package nl.fontys.s3.rentride_be.business.useCases.booking;

import nl.fontys.s3.rentride_be.domain.booking.CreateBookingRequest;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingResponse;

public interface CreateBookingUseCase {
    CreateBookingResponse createBooking(CreateBookingRequest request);
}
