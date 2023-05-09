package org.mint.smallcloud.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
@Getter
public enum ExceptionStatus {
    NOT_FOUND_COMMON_MEMBER(HttpStatus.FORBIDDEN, "회원이 존재하지 않습니다."),
    NOT_FOUND_ADMIN_MEMBER(HttpStatus.FORBIDDEN, "관리자가 존재하지 않습니다."),
    NOT_FOUND_MEMBER(HttpStatus.FORBIDDEN, "아이디를 찾을 수 없습니다."),
    WRONG_PASSWORD(HttpStatus.FORBIDDEN, "비밀번호가 다릅니다."),
    NOT_VALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "JWT토큰이 올바르지 않습니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT토큰이 만료되었습니다."),
    NOT_FOUND_JWT_TOKEN(HttpStatus.FORBIDDEN, "JWT토큰을 찾을 수 없습니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.FORBIDDEN, "아이디가 이미 존재합니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error."),
    NOT_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "refresh토큰이 아닙니다.");

    ExceptionStatus(HttpStatus status, String message) {
        responseDto = ErrorResponseDto.builder()
            .statusCode(status.value())
            .message(message)
            .error(status.getReasonPhrase())
            .build();
    }

    @JsonUnwrapped
    private final ErrorResponseDto responseDto;
}