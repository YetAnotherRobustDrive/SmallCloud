package org.mint.smallcloud.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> serviceExceptionHandler(ServiceException e) {
        ErrorResponseDto responseDto = e.getStatus().getResponseDto();
        log.info("called ExceptionController for {}", responseDto.getError());
        return ResponseEntity
                .status(responseDto.getStatusCode())
                .body(e.getStatus());
    }
}