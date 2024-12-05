package nl.fontys.s3.rentride_be.business.impl.auction;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.auction.GetAllAuctionsUseCase;
import nl.fontys.s3.rentride_be.domain.auction.Auction;
import nl.fontys.s3.rentride_be.persistance.AuctionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllAuctionsUseCaseImpl implements GetAllAuctionsUseCase {
    private AuctionRepository auctionRepository;
    @Override
    public List<Auction> getAllAuctions() {
        return auctionRepository
                .findAll()
                .stream().map(AuctionConverter::convert)
                .toList();
    }
}
