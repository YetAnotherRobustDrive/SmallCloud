package org.mint.smallcloud.exception;

public class ErrorResponseDto {
    private final int statusCode;
    private final String message;
    private final String error;

    public ErrorResponseDto(int statusCode, String message, String error) {
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
    }

    public static ErrorResponseDtoBuilder builder() {
        return new ErrorResponseDtoBuilder();
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getMessage() {
        return this.message;
    }

    public String getError() {
        return this.error;
    }

    public static class ErrorResponseDtoBuilder {
        private int statusCode;
        private String message;
        private String error;

        ErrorResponseDtoBuilder() {
        }

        public ErrorResponseDtoBuilder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ErrorResponseDtoBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseDtoBuilder error(String error) {
            this.error = error;
            return this;
        }

        public ErrorResponseDto build() {
            return new ErrorResponseDto(this.statusCode, this.message, this.error);
        }

        public String toString() {
            return "ErrorResponseDto.ErrorResponseDtoBuilder(statusCode=" + this.statusCode + ", message=" + this.message + ", error=" + this.error + ")";
        }
    }
}
