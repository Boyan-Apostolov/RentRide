package nl.fontys.s3.rentride_be.business.use_cases.auction;

import nl.fontys.s3.rentride_be.domain.auction.BidResponse;
import nl.fontys.s3.rentride_be.domain.auction.NotificationMessage;

public interface AuctionMessengerUseCase {
     void broadcastNewBid(BidResponse newBid);

     void convertAndSend(String path, NotificationMessage message);

     void broadcastNewUser(Long auctionId, String userName);
}
