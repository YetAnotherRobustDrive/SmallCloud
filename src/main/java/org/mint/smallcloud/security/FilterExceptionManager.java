package org.mint.smallcloud.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ErrorResponseDto;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilterExceptionManager {
    private static final String ATTRIBUTE_NAME = "exception";
    private static final String CONTENT_TYPE_NAME = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";
    private static final String UTF_8 = "utf-8";
    private final ObjectMapper objectMapper;
    public void addException(HttpServletRequest request, Exception exception) {
        request.setAttribute(ATTRIBUTE_NAME, exception);
    }

    public void handleExceptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
