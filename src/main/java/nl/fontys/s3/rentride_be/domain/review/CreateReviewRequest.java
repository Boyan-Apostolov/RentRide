package nl.fontys.s3.rentride_be.domain.review;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateReviewRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long bookingId;

    @NotNull
    private String text;

    @NotNull
    private Integer valueForMoney;

    @NotNull
    private Integer carCondition;

    @NotNull
    private Integer carSpeed;
}
