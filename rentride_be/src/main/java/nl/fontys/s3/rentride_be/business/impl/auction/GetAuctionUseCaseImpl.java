package nl.fontys.s3.rentride_be.business.impl.auction;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.auction.GetAuctionUseCase;
import nl.fontys.s3.rentride_be.domain.auction.Auction;
import nl.fontys.s3.rentride_be.persistance.AuctionRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetAuctionUseCaseImpl implements GetAuctionUseCase {

    private AuctionRepository auctionRepository;
    @Override
    public Auction getAuction(Long auctionId) {

        return AuctionConverter.convert(
                this.auctionRepository.findById(auctionId).orElse(null)
        );
    }
}
