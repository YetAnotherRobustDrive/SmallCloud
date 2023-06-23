package org.mint.smallcloud.exception;

import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {
    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(ExceptionController.class);

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> serviceExceptionHandler(ServiceException e) {
        ErrorResponseDto responseDto = e.getStatus().getResponseDto();
        log.debug("called serviceExceptionHandler for {}: {}", responseDto.getError(), responseDto.getMessage());
        return ResponseEntity
            .status(responseDto.getStatusCode())
            .body(e.getStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errors);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
        ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations()
            .forEach(e -> {
                String fullFieldName = e.getPropertyPath().toString();
                String fieldName = fullFieldName.substring(fullFieldName.lastIndexOf('.') + 1);
                String errorMessage = e.getMessage();
                errors.put(fieldName, errorMessage);
            });
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errors);
    }

}