package nl.fontys.s3.rentride_be.business.use_cases.auction;

import nl.fontys.s3.rentride_be.domain.auction.Auction;

public interface GetAuctionUseCase {
    Auction getAuction(Long auctionId);
}
