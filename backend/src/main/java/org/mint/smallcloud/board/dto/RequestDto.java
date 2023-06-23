package org.mint.smallcloud.board.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RequestDto {

    @Size(min = 1, max = 500, message = "답변 내용은 500자 이하로 작성해 주세요.")
    @NotBlank(message = "답변 내용은 필수로 들어가야 합니다.")
    private final String content;

    @NotNull(message = "질문 테이블의 정보가 들어가야 합니다.")
    private final Long questionId;

    public RequestDto(@Size(min = 1, max = 500, message = "답변 내용은 500자 이하로 작성해 주세요.") @NotBlank(message = "답변 내용은 필수로 들어가야 합니다.") String content, @NotNull(message = "질문 테이블의 정보가 들어가야 합니다.") Long questionId) {
        this.content = content;
        this.questionId = questionId;
    }

    public static RequestDtoBuilder builder() {
        return new RequestDtoBuilder();
    }

    public @Size(min = 1, max = 500, message = "답변 내용은 500자 이하로 작성해 주세요.") @NotBlank(message = "답변 내용은 필수로 들어가야 합니다.") String getContent() {
        return this.content;
    }

    public @NotNull(message = "질문 테이블의 정보가 들어가야 합니다.") Long getQuestionId() {
        return this.questionId;
    }

    public static class RequestDtoBuilder {
        private @Size(min = 1, max = 500, message = "답변 내용은 500자 이하로 작성해 주세요.") @NotBlank(message = "답변 내용은 필수로 들어가야 합니다.") String content;
        private @NotNull(message = "질문 테이블의 정보가 들어가야 합니다.") Long questionId;

        RequestDtoBuilder() {
        }

        public RequestDtoBuilder content(@Size(min = 1, max = 500, message = "답변 내용은 500자 이하로 작성해 주세요.") @NotBlank(message = "답변 내용은 필수로 들어가야 합니다.") String content) {
            this.content = content;
            return this;
        }

        public RequestDtoBuilder questionId(@NotNull(message = "질문 테이블의 정보가 들어가야 합니다.") Long questionId) {
            this.questionId = questionId;
            return this;
        }

        public RequestDto build() {
            return new RequestDto(this.content, this.questionId);
        }

        public String toString() {
            return "RequestDto.RequestDtoBuilder(content=" + this.content + ", questionId=" + this.questionId + ")";
        }
    }
}

