package nl.fontys.s3.rentride_be.domain.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;

@Data
@Builder
@AllArgsConstructor
public class CreateBookingResponse {
    private Long bookingId;
    private BookingEntity booking;
}
