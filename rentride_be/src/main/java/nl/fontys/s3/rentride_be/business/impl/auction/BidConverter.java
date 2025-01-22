package nl.fontys.s3.rentride_be.business.impl.auction;

import nl.fontys.s3.rentride_be.business.impl.user.UserConverter;
import nl.fontys.s3.rentride_be.domain.auction.Bid;
import nl.fontys.s3.rentride_be.persistance.entity.BidEntity;

public final class BidConverter {
    private BidConverter() {}

    public static Bid convert(BidEntity bidEntity) {
        if(bidEntity == null) return null;

        return Bid.builder()
                .id(bidEntity.getId())
                .amount(bidEntity.getAmount())
                .dateTime(bidEntity.getDateTime())
                .user(UserConverter.convert(bidEntity.getUser()))
//                .auction(bidEntity.getAuction())
                .build();
    }
}
