package nl.fontys.s3.rentride_be.domain.booking;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {

    @NotNull
    @Min(0)
    @Max(2)
    private int coverage;
    @NotNull
    @Min(0)
    private Long carId;
    @NotNull
    @Min(0)
    private Long fromCityId;
    @NotNull
    @Min(0)
    private Long toCityId;
    @NotNull
    private LocalDateTime startDateTime;
    @NotNull
    private LocalDateTime endDateTime;
    @Min(0)

    private double totalPrice;
    @Min(0)
    private long userId;
}
