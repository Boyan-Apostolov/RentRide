package nl.fontys.s3.rentride_be.configuration.security.auth;

import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class RequestAuthenticatedUserProvider {

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AccessToken getAuthenticatedUserInRequest() {
        final SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }

        final Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }

        final Object details = authentication.getDetails();
        if (!(details instanceof AccessToken)) {
            return null;
        }

        return (AccessToken) authentication.getDetails();
    }
}
