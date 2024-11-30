package nl.fontys.s3.rentride_be.business.use_cases.auction;

import nl.fontys.s3.rentride_be.domain.auction.CreateAuctionRequest;
import nl.fontys.s3.rentride_be.domain.auction.CreateAuctionResponse;

public interface CreateAuctionUseCase {
     CreateAuctionResponse createAuction(CreateAuctionRequest createAuctionRequest);
}
