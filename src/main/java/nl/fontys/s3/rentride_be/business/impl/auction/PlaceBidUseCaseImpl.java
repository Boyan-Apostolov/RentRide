package nl.fontys.s3.rentride_be.business.impl.auction;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.impl.user.UserConverter;
import nl.fontys.s3.rentride_be.business.use_cases.auction.AuctionMessengerUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.auction.PlaceBidUseCase;
import nl.fontys.s3.rentride_be.domain.auction.BidRequest;
import nl.fontys.s3.rentride_be.domain.auction.BidResponse;
import nl.fontys.s3.rentride_be.persistance.AuctionRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.AuctionEntity;
import nl.fontys.s3.rentride_be.persistance.entity.BidEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlaceBidUseCaseImpl implements PlaceBidUseCase {
    private AuctionRepository auctionRepository;
    private UserRepository userRepository;
    private AuctionMessengerUseCase auctionMessengerUseCase;

    @Override
    public BidResponse placeBid(BidRequest bidRequest) {
        Optional<AuctionEntity> auction = auctionRepository.findById(bidRequest.getAuctionId());
        if (auction.isEmpty()) throw new NotFoundException("Bid->Auction");

        AuctionEntity auctionEntity = auction.get();
        if (auctionEntity.getWinnerUser() != null)
            throw new InvalidOperationException("Auction already has winner");

        Optional<UserEntity> user = userRepository.findById(bidRequest.getUserId());
        if (user.isEmpty()) throw new NotFoundException("Bid->User");
        UserEntity userEntity = user.get();

        BidEntity currentHighestBid = auctionEntity.getBids().isEmpty()
                ? null
                : auctionEntity.getBids().get(auctionEntity.getBids().size() - 1);

        boolean shouldCreateBid = currentHighestBid == null
                || (bidRequest.getBidAmount() > currentHighestBid.getAmount()
                && !Objects.equals(currentHighestBid.getUser().getId(), bidRequest.getUserId()));

        if (shouldCreateBid) {

            BidEntity bidEntity = BidEntity.builder()
                    .amount(bidRequest.getBidAmount())
                    .dateTime(LocalDateTime.now())
                    .user(userEntity)
                    .auction(auctionEntity)
                    .build();

            auctionEntity.getBids().add(bidEntity);
            auctionRepository.save(auctionEntity);

            BidResponse bidResponse = BidResponse.builder()
                    .amount(bidEntity.getAmount())
                    .user(UserConverter.convert(bidEntity.getUser()))
                    .auctionId(auctionEntity.getId())
                    .dateTime(bidEntity.getDateTime())
                    .build();

            auctionMessengerUseCase.broadcastNewBid(bidResponse);

            return bidResponse;
        } else {
            return null;
        }
    }
}
