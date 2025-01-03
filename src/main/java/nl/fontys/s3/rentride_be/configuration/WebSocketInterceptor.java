package nl.fontys.s3.rentride_be.configuration;

import io.jsonwebtoken.*;
import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class WebSocketInterceptor implements ChannelInterceptor {

    private final String SECRET_KEY;

    // Constructor injection for secret key
    public WebSocketInterceptor(@Value("${jwt.secret}") String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && Objects.equals(accessor.getDestination(), "/auction/bid")) {
            String accessToken = accessor.getFirstNativeHeader("accessToken");

            if (accessToken == null) {
                System.out.println("Missing access token for WebSocket message.");
                throw new InvalidOperationException("Missing authentication token!");
            }

            if (validateToken(accessToken)) {
                return message;
            }

            throw new InvalidOperationException("User is not authenticated for bidding!");
        }
        return message;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}