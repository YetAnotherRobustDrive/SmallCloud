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
    NOT_FOUND_COMMON_MEMBER(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    NOT_FOUND_ADMIN_MEMBER(HttpStatus.NOT_FOUND, "관리자가 존재하지 않습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "아이디를 찾을 수 없습니다."),
    WRONG_PASSWORD(HttpStatus.FORBIDDEN, "비밀번호가 다릅니다."),
    NOT_VALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "JWT토큰이 올바르지 않습니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT토큰이 만료되었습니다."),
    NOT_FOUND_JWT_TOKEN(HttpStatus.BAD_REQUEST, "JWT토큰을 찾을 수 없습니다."),
    NOT_ACCESS_JWT_TOKEN(HttpStatus.BAD_REQUEST, "access토큰이 아닙니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.FORBIDDEN, "아이디가 이미 존재합니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error."),
    NOT_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "refresh토큰이 아닙니다."),
    NOT_FOUND_GROUP_NAME(HttpStatus.NOT_FOUND, "해당 이름을 가진 그룹을 찾을 수 없습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 경로에 파일이 없습니다."),
    NOT_FOUND_DIRECTORY(HttpStatus.NOT_FOUND, "해당 경로에 디렉토리가 없습니다."),
    ALREADY_EXISTS_DIRECTORY(HttpStatus.FORBIDDEN, "해당 경로에 이미 디렉토리가 존재합니다."),
    NOT_FOUND_INQUIRY(HttpStatus.NOT_FOUND, "문의를 찾을 수 없습니다."),
    NOT_FOUND_QUESTION(HttpStatus.NOT_FOUND, "질문을 찾을 수 없습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터가 올바르지 않습니다."),
    GROUP_NAME_ALREADY_EXISTS(HttpStatus.FORBIDDEN, "이미 그룹이 존재합니다"),
    ALREADY_EXISTS_LABEL(HttpStatus.FORBIDDEN, "이미 존재하는 라벨입니다."),
    NOT_FOUND_LABEL(HttpStatus.NOT_FOUND, "라벨을 찾을 수 없습니다."),
    NOT_FOUND_OWNER(HttpStatus.NOT_FOUND, "소유자를 찾을 수 없습니다."),
    USER_LOCKED(HttpStatus.FORBIDDEN, "잠긴 유저입니다."),
    NOT_FOUND_NOTIFICATION(HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다."),
    NOT_FOUND_ANSWER(HttpStatus.NOT_FOUND, "답변을 찾을 수 없습니다."),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    FILE_UPLOAD_FAIL(HttpStatus.CONFLICT, "파일 업로드 중 오류가 생겼습니다."),
    ;

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