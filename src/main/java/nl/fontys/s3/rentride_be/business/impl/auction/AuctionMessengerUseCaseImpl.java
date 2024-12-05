package nl.fontys.s3.rentride_be.business.impl.auction;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.auction.AuctionMessengerUseCase;
import nl.fontys.s3.rentride_be.domain.auction.BidResponse;
import nl.fontys.s3.rentride_be.domain.auction.NotificationMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuctionMessengerUseCaseImpl implements AuctionMessengerUseCase {
    private final SimpMessagingTemplate messagingTemplate;

    public void convertAndSend(String path, NotificationMessage message){
        messagingTemplate.convertAndSend(path, message);
    }

    public void broadcastNewBid(BidResponse newBid) {
        messagingTemplate.convertAndSend("/auction/" + newBid.getAuctionId() + "/bids", newBid);
    }

    public void broadcastNewUser(Long auctionId, String userName) {
        messagingTemplate.convertAndSend("/auction/" + auctionId + "/new-user", userName);

    }
}
