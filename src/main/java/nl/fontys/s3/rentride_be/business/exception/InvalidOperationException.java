package nl.fontys.s3.rentride_be.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidOperationException extends ResponseStatusException {
    public InvalidOperationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
