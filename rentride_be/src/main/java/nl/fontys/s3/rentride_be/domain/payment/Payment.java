package nl.fontys.s3.rentride_be.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.rentride_be.domain.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private long id;
    private String description;
    private double amount;
    private User user;
    private boolean isPaid;
    private String stripeLink;
    private LocalDateTime createdOn;
}
