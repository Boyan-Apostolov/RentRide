package nl.fontys.s3.rentride_be.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {
    public NotFoundException(String className) {
        super(HttpStatus.BAD_REQUEST, className + "_NOT_FOUND");
    }

}
