package org.mint.smallcloud;

public class ResponseDto<T> {
    private T result;

    ResponseDto(T result) {
        this.result = result;
    }

    public static <T> ResponseDtoBuilder<T> builder() {
        return new ResponseDtoBuilder<T>();
    }

    public T getResult() {
        return this.result;
    }

    public static class ResponseDtoBuilder<T> {
        private T result;

        ResponseDtoBuilder() {
        }

        public ResponseDtoBuilder<T> result(T result) {
            this.result = result;
            return this;
        }

        public ResponseDto<T> build() {
            return new ResponseDto<T>(this.result);
        }

        public String toString() {
            return "ResponseDto.ResponseDtoBuilder(result=" + this.result + ")";
        }
    }
}
