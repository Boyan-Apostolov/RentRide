package nl.fontys.s3.rentride_be.domain.auction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationMessage {
    private String message;
    private Long auctionId;
    private Long userId;
    private Double currentBid;
}