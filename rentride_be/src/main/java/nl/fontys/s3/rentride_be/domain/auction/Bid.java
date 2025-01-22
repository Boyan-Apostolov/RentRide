package nl.fontys.s3.rentride_be.domain.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.rentride_be.domain.user.User;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
public class Bid {
    private Long id;

    private LocalDateTime dateTime;

    private double amount;

    private Auction auction;

    private User user;
}
