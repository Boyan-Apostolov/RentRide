package nl.fontys.s3.rentride_be.domain.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateBookingResponse {
    private Long bookingId;
}
