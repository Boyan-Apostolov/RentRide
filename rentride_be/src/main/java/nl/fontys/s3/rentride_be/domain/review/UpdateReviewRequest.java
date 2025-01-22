package nl.fontys.s3.rentride_be.domain.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.rentride_be.domain.booking.Booking;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateReviewRequest {
    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    private String text;

    private Booking booking;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer valueForMoney;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer carCondition;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer carSpeed;
}
