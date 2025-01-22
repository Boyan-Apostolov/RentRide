package nl.fontys.s3.rentride_be.configuration.security.token;

import java.util.Set;

public interface AccessToken {
    String getSubject();

    Long getUserId();

    Set<String> getRoles();

    boolean hasRole(String roleName);
}
