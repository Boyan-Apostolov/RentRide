package nl.fontys.s3.rentride_be.domain.auction;

import lombok.*;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Auction {
    private Long id;

    private String description;

    private double minBidAmount;

    private LocalDateTime endDateTime;

    private int canBeClaimed;

    private Car car;

    private User winnerUser;

    private List<Bid> bids;
}
