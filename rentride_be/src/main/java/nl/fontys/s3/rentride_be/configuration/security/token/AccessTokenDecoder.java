package nl.fontys.s3.rentride_be.configuration.security.token;


public interface AccessTokenDecoder {
    AccessToken decode(String accessTokenEncoded);
}
