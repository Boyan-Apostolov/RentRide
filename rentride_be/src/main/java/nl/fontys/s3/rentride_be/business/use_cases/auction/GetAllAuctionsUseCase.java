package nl.fontys.s3.rentride_be.business.use_cases.auction;

import nl.fontys.s3.rentride_be.domain.auction.Auction;

import java.util.List;

public interface GetAllAuctionsUseCase {
    List<Auction> getAllAuctions();
    List<Auction> getAllAuctions(int page);

    Long getCount();
}
