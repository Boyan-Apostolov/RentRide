package nl.fontys.s3.rentride_be.configuration.exceptionhandler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
@Slf4j
public class RestCustomExceptionHandler extends ResponseEntityExceptionHandler {
    private static final URI VALIDATION_ERROR_TYPE = URI.create("/validation-error");

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException error, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("MethodArgumentNotValidException with status {} occurred.", HttpStatus.BAD_REQUEST, error);
        final List<ValidationErrorDTO> errors = convertToErrorsList(error);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(convertToProblemDetail(errors));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ProblemDetail handleConstraintViolationException(final ConstraintViolationException error) {
        log.error("ConstraintViolationException with status {} occurred.", HttpStatus.BAD_REQUEST, error);
        final List<ValidationErrorDTO> errors = convertToErrorsList(error);
        return convertToProblemDetail(errors);
    }

    @ExceptionHandler(value = {ResponseStatusException.class})
    public ProblemDetail handleResponseStatusException(final ResponseStatusException error) {
        log.error("ResponseStatusException with status {} occurred.", error.getStatusCode(), error);
        final List<ValidationErrorDTO> errors = error.getReason() != null ?
                List.of(new ValidationErrorDTO(null, error.getReason()))
                : Collections.emptyList();
        return convertToProblemDetail(error.getStatusCode(), errors);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ProblemDetail handleUnknownRuntimeError(final RuntimeException error) {
        log.error("Internal server error occurred.", error);
        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ProblemDetail convertToProblemDetail(final List<ValidationErrorDTO> errors) {
        return convertToProblemDetail(HttpStatus.BAD_REQUEST, errors);
    }

    private ProblemDetail convertToProblemDetail(HttpStatusCode statusCode, List<ValidationErrorDTO> errors) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
        problemDetail.setDetail("Invalid request");
        problemDetail.setProperty("errors", errors);
        problemDetail.setType(VALIDATION_ERROR_TYPE);
        return problemDetail;
    }

    private List<ValidationErrorDTO> convertToErrorsList(final MethodArgumentNotValidException error) {
        final BindingResult bindingResult = error.getBindingResult();
        final List<ValidationErrorDTO> result = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .forEach(validationError -> {
                        if (validationError instanceof final FieldError fieldError) {
                            result.add(new ValidationErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()));
                        } else {
                            result.add(new ValidationErrorDTO(validationError.getObjectName(), validationError.getDefaultMessage()));
                        }
                    });
        } else {
            log.warn("MethodArgumentNotValidException without binding result errors", error);
        }
        return result;
    }

    private List<ValidationErrorDTO> convertToErrorsList(final ConstraintViolationException error) {
        if (CollectionUtils.isEmpty(error.getConstraintViolations())) {
            log.warn("Empty constraints violation for error: {}", error.getMessage());
            return Collections.emptyList();
        }

        final List<ValidationErrorDTO> result = new ArrayList<>();
        error.getConstraintViolations().forEach(constraintViolation -> {
                    final String field = constraintViolation.getPropertyPath() != null ? constraintViolation.getPropertyPath().toString() : "unknown field";
                    result.add(new ValidationErrorDTO(field, constraintViolation.getMessage()));
                }
        );
        return result;
    }

    private record ValidationErrorDTO(String field, String error) {
    }
}
