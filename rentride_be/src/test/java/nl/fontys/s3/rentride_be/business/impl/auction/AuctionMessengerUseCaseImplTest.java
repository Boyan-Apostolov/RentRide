package nl.fontys.s3.rentride_be.business.impl.auction;

import nl.fontys.s3.rentride_be.domain.auction.BidResponse;
import nl.fontys.s3.rentride_be.domain.auction.NotificationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionMessengerUseCaseImplTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private AuctionMessengerUseCaseImpl auctionMessengerUseCase;

    private BidResponse bidResponse;
    private NotificationMessage notificationMessage;

    @BeforeEach
    void setUp() {
        bidResponse = BidResponse.builder()
                .auctionId(5L)
                .amount(1000.0)
                .user(null)
                .build();

        notificationMessage = NotificationMessage.builder()
                .message("Auction ended!")
                .build();
    }

    @Test
    void convertAndSend_ShouldSendNotificationMessage() {
        String path = "/topic/auction/notifications";
        auctionMessengerUseCase.convertAndSend(path, notificationMessage);

        verify(messagingTemplate, times(1)).convertAndSend(path, notificationMessage);
    }

    @Test
    void broadcastNewBid_ShouldSendBidResponseToCorrectPath() {
        auctionMessengerUseCase.broadcastNewBid(bidResponse);

        String expectedPath = "/auction/5/bids";
        verify(messagingTemplate, times(1)).convertAndSend(expectedPath, bidResponse);
    }

    @Test
    void broadcastNewUser_ShouldSendUserNameToCorrectPath() {
        Long auctionId = 10L;
        String userName = "john_doe";

        auctionMessengerUseCase.broadcastNewUser(auctionId, userName);

        String expectedPath = "/auction/10/new-user";
        verify(messagingTemplate, times(1)).convertAndSend(expectedPath, userName);
    }
}