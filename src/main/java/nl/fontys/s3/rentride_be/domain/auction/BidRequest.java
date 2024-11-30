package nl.fontys.s3.rentride_be.domain.auction;

import lombok.Data;

@Data
public class BidRequest {
    private Long auctionId;
    private Long userId;
    private Double bidAmount;
}