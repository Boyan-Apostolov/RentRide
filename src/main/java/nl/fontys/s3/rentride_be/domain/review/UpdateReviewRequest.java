package nl.fontys.s3.rentride_be.domain.review;

import jakarta.validation.constraints.NotNull;
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
    private String text;

    private Booking booking;

    @NotNull
    private Integer valueForMoney;

    @NotNull
    private Integer carCondition;

    @NotNull
    private Integer carSpeed;
}
