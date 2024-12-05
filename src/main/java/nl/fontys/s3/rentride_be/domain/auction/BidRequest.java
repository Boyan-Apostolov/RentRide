package nl.fontys.s3.rentride_be.domain.auction;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BidRequest {
    private Long auctionId;
    private Long userId;
    private Double bidAmount;
}