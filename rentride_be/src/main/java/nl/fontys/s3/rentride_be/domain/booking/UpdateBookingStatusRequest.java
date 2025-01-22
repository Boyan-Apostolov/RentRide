package nl.fontys.s3.rentride_be.domain.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookingStatusRequest {
    private Long bookingId;
    private int status;
}
