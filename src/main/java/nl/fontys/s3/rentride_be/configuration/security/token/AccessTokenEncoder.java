package nl.fontys.s3.rentride_be.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
