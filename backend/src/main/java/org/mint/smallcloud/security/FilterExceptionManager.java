package org.mint.smallcloud.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mint.smallcloud.exception.ErrorResponseDto;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FilterExceptionManager {
    private static final String ATTRIBUTE_NAME = "exception";
    private static final String CONTENT_TYPE_NAME = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";
    private static final String UTF_8 = "utf-8";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FilterExceptionManager.class);
    private final ObjectMapper objectMapper;

    public FilterExceptionManager(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void addException(HttpServletRequest request, Exception exception) {
        if (request.getAttribute(ATTRIBUTE_NAME) != null)
            return;
        request.setAttribute(ATTRIBUTE_NAME, exception);
    }

    public void handleExceptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Filter Service Exception");
        Object object = request.getAttribute(ATTRIBUTE_NAME);
        ErrorResponseDto responseDto;
        if (object == null)
            responseDto = ExceptionStatus.NOT_FOUND_JWT_TOKEN.getResponseDto();
        else if (object instanceof ServiceException) {
            responseDto = ((ServiceException) object).getStatus().getResponseDto();
        } else {
            HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = ErrorResponseDto.builder()
                .statusCode(httpStatus.value())
                .message("Unexpected filter exception")
                .error(httpStatus.getReasonPhrase())
                .build();
        }
        setResponseDto(response, responseDto);
    }

    public void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Filter Permission Error");
        ErrorResponseDto responseDto = ExceptionStatus.NO_PERMISSION.getResponseDto();
        setResponseDto(response, responseDto);
    }

    private void setResponseDto(HttpServletResponse response, ErrorResponseDto responseDto) throws IOException {
        response.setStatus(responseDto.getStatusCode());
        response.setHeader(CONTENT_TYPE_NAME, CONTENT_TYPE);
        response.setCharacterEncoding(UTF_8);
        response.getWriter()
            .write(objectMapper.writeValueAsString(responseDto));
    }
}
