package org.mint.smallcloud.notification.dto;

import java.time.LocalDateTime;

public class NotificationResponseDto {
    Long id;
    String content;
    LocalDateTime localDateTime;

    public NotificationResponseDto(Long id, String content, LocalDateTime localDateTime) {
        this.id = id;
        this.content = content;
        this.localDateTime = localDateTime;
    }

    public static NotificationResponseDtoBuilder builder() {
        return new NotificationResponseDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public LocalDateTime getLocalDateTime() {
        return this.localDateTime;
    }

    public static class NotificationResponseDtoBuilder {
        private Long id;
        private String content;
        private LocalDateTime localDateTime;

        NotificationResponseDtoBuilder() {
        }

        public NotificationResponseDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public NotificationResponseDtoBuilder content(String content) {
            this.content = content;
            return this;
        }

        public NotificationResponseDtoBuilder localDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
            return this;
        }

        public NotificationResponseDto build() {
            return new NotificationResponseDto(this.id, this.content, this.localDateTime);
        }

        public String toString() {
            return "NotificationResponseDto.NotificationResponseDtoBuilder(id=" + this.id + ", content=" + this.content + ", localDateTime=" + this.localDateTime + ")";
        }
    }
}
