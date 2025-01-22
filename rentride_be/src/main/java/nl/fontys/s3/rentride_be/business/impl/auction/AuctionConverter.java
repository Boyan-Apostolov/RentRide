package nl.fontys.s3.rentride_be.business.impl.auction;

import nl.fontys.s3.rentride_be.business.impl.car.CarConverter;
import nl.fontys.s3.rentride_be.business.impl.user.UserConverter;
import nl.fontys.s3.rentride_be.domain.auction.Auction;
import nl.fontys.s3.rentride_be.persistance.entity.AuctionEntity;

public final class AuctionConverter {
    private AuctionConverter() {}

    public static Auction convert(AuctionEntity auctionEntity) {
        if(auctionEntity == null) return null;

        return Auction.builder()
                .id(auctionEntity.getId())
                .bids(auctionEntity.getBids().stream().map(BidConverter::convert).toList())
                .endDateTime(auctionEntity.getEndDateTime())
                .winnerUser(UserConverter.convert(auctionEntity.getWinnerUser()))
                .car(CarConverter.convert(auctionEntity.getCar()))
                .description(auctionEntity.getDescription())
                .canBeClaimed(auctionEntity.getCanBeClaimed())
                .minBidAmount(auctionEntity.getMinBidAmount())
                .build();
    }
}

