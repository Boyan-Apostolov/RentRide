package nl.fontys.s3.rentride_be.business.impl.auction;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auction.UpdateAuctionCanBeClaimedUseCase;
import nl.fontys.s3.rentride_be.persistance.AuctionRepository;
import nl.fontys.s3.rentride_be.persistance.entity.AuctionEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateAuctionCanBeClaimedUseCaseImpl implements UpdateAuctionCanBeClaimedUseCase {
    private AuctionRepository auctionRepository;

    public void updateState(Long auctionId, int canBeClaimed) {
        Optional<AuctionEntity> auctionEntity = auctionRepository.findById(auctionId);
        if (auctionEntity.isEmpty()) throw new NotFoundException("UpdateAuctionClaimed->Auction");

        AuctionEntity auction = auctionEntity.get();

        auction.setCanBeClaimed(canBeClaimed);

        auctionRepository.save(auction);
    }
}
