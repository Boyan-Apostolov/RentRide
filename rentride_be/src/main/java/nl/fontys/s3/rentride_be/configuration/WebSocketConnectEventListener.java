package nl.fontys.s3.rentride_be.configuration;


import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.auction.AuctionMessengerUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.user.GetUserUseCase;
import nl.fontys.s3.rentride_be.domain.user.User;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketConnectEventListener {
    private final GetUserUseCase getUserUseCase;
    private final AuctionMessengerUseCase auctionMessengerUseCase;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        Map<String, Long> ids = getUserAndAuctionId(event);

        Long userId = ids.get("userId");
        User newJoinedUser = getUserUseCase.getUser(userId);

        Long auctionId = ids.get("auctionId");

        auctionMessengerUseCase.broadcastNewUser(auctionId, newJoinedUser.getName());
    }

    private static Map<String, Long> getUserAndAuctionId(SessionConnectedEvent event) {
        MessageHeaderAccessor headerAccessor =
                MessageHeaderAccessor.getAccessor(event.getMessage().getHeaders(),
                        MessageHeaderAccessor.class);
        StompHeaderAccessor stompHeaderAccessor = MessageHeaderAccessor.getAccessor(
                (Message<?>) headerAccessor.getHeader("simpConnectMessage"),
                StompHeaderAccessor.class);
        String userId = stompHeaderAccessor.getNativeHeader("userId").get(0);
        String auctionId = stompHeaderAccessor.getNativeHeader("auctionId").get(0);

        Map<String, Long> result = new HashMap<>();

        result.put("userId", Long.parseLong(userId));
        result.put("auctionId", Long.parseLong(auctionId));

        return result;
    }
}