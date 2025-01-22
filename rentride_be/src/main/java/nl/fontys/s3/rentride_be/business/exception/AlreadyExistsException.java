package nl.fontys.s3.rentride_be.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AlreadyExistsException extends ResponseStatusException {
    public AlreadyExistsException(String entityName) {
        super(HttpStatus.BAD_REQUEST, entityName + "ALREADY_EXISTS");
    }
}
