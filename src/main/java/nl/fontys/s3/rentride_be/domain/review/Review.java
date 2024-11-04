package nl.fontys.s3.rentride_be.domain.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.domain.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    private Long id;

    private LocalDateTime createdOn;

    private String text;

    private User user;

    private Booking booking;

    private Integer valueForMoney;

    private Integer carCondition;

    private Integer carSpeed;
}
