package nl.fontys.s3.rentride_be.business.useCases.booking;

import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;

public interface UpdateBookingStatusUseCase {
    void updateBookingStatus(Long bookingId, BookingStatus newStatus);
}
