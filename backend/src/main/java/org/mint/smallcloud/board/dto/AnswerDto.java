package org.mint.smallcloud.board.dto;

import java.time.LocalDateTime;

public class AnswerDto {
    private final Long id;
    private final String content;
    private final LocalDateTime createdDate;

    public AnswerDto(Long id, String content, LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
    }

    public static AnswerDtoBuilder builder() {
        return new AnswerDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public static class AnswerDtoBuilder {
        private Long id;
        private String content;
        private LocalDateTime createdDate;

        AnswerDtoBuilder() {
        }

        public AnswerDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AnswerDtoBuilder content(String content) {
            this.content = content;
            return this;
        }

        public AnswerDtoBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public AnswerDto build() {
            return new AnswerDto(this.id, this.content, this.createdDate);
        }

        public String toString() {
            return "AnswerDto.AnswerDtoBuilder(id=" + this.id + ", content=" + this.content + ", createdDate=" + this.createdDate + ")";
        }
    }
}
