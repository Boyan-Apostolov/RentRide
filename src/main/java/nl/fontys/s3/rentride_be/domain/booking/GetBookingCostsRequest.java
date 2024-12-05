package nl.fontys.s3.rentride_be.domain.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBookingCostsRequest {
    private long carId;
    private long fromCityId;
    private long toCityId;

    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
}
