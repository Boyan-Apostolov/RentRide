package nl.fontys.s3.rentride_be.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CityAlreadyExistsException extends ResponseStatusException {
    public CityAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, "CITY_ALREADY_EXISTS");
    }
}
