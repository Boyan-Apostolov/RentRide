package nl.fontys.s3.rentride_be.domain.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NotificationMessage {
    private String message;
    private Long auctionId;
    private Long userId;
    private Double currentBid;
}