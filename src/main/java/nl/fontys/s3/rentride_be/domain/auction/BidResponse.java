package nl.fontys.s3.rentride_be.domain.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.rentride_be.domain.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BidResponse {
    private Long auctionId;
    private User user;
    private LocalDateTime dateTime;
    private Double amount;
}

