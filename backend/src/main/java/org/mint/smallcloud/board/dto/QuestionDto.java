package org.mint.smallcloud.board.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class QuestionDto {
    private final Long id;

    private final String title;

    @Size(min = 1)
    @NotBlank(message = "연락처는 필수로 들어가야 합니다.")
    private final String contact;

    @Size(min = 1, max = 500, message = "문의 내용은 500자 이하로 작성해 주세요.")
    @NotBlank(message = "문의 내용은 필수로 들어가야 합니다.")
    private final String content;

    private final String writer;

    private final Long answerId;

    public QuestionDto(Long id, String title, @Size(min = 1) @NotBlank(message = "연락처는 필수로 들어가야 합니다.") String contact, @Size(min = 1, max = 500, message = "문의 내용은 500자 이하로 작성해 주세요.") @NotBlank(message = "문의 내용은 필수로 들어가야 합니다.") String content, String writer, Long answerId) {
        this.id = id;
        this.title = title;
        this.contact = contact;
        this.content = content;
        this.writer = writer;
        this.answerId = answerId;
    }

    public static QuestionDtoBuilder builder() {
        return new QuestionDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public @Size(min = 1) @NotBlank(message = "연락처는 필수로 들어가야 합니다.") String getContact() {
        return this.contact;
    }

    public @Size(min = 1, max = 500, message = "문의 내용은 500자 이하로 작성해 주세요.") @NotBlank(message = "문의 내용은 필수로 들어가야 합니다.") String getContent() {
        return this.content;
    }

    public String getWriter() {
        return this.writer;
    }

    public Long getAnswerId() {
        return this.answerId;
    }

    public static class QuestionDtoBuilder {
        private Long id;
        private String title;
        private @Size(min = 1) @NotBlank(message = "연락처는 필수로 들어가야 합니다.") String contact;
        private @Size(min = 1, max = 500, message = "문의 내용은 500자 이하로 작성해 주세요.") @NotBlank(message = "문의 내용은 필수로 들어가야 합니다.") String content;
        private String writer;
        private Long answerId;

        QuestionDtoBuilder() {
        }

        public QuestionDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public QuestionDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public QuestionDtoBuilder contact(@Size(min = 1) @NotBlank(message = "연락처는 필수로 들어가야 합니다.") String contact) {
            this.contact = contact;
            return this;
        }

        public QuestionDtoBuilder content(@Size(min = 1, max = 500, message = "문의 내용은 500자 이하로 작성해 주세요.") @NotBlank(message = "문의 내용은 필수로 들어가야 합니다.") String content) {
            this.content = content;
            return this;
        }

        public QuestionDtoBuilder writer(String writer) {
            this.writer = writer;
            return this;
        }

        public QuestionDtoBuilder answerId(Long answerId) {
            this.answerId = answerId;
            return this;
        }

        public QuestionDto build() {
            return new QuestionDto(this.id, this.title, this.contact, this.content, this.writer, this.answerId);
        }

        public String toString() {
            return "QuestionDto.QuestionDtoBuilder(id=" + this.id + ", title=" + this.title + ", contact=" + this.contact + ", content=" + this.content + ", writer=" + this.writer + ", answerId=" + this.answerId + ")";
        }
    }
}

