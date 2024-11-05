package nl.fontys.s3.rentride_be.domain.review;

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

    private String text;

    private Booking booking;

    private Integer valueForMoney;

    private Integer carCondition;

    private Integer carSpeed;
}
