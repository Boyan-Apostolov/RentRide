package nl.fontys.s3.rentride_be.business.use_cases.auction;

import nl.fontys.s3.rentride_be.domain.auction.BidRequest;
import nl.fontys.s3.rentride_be.domain.auction.BidResponse;

public interface PlaceBidUseCase {
    BidResponse placeBid(BidRequest bidRequest);
}
