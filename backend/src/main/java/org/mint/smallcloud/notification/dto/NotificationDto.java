package org.mint.smallcloud.notification.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class NotificationDto {
    @Size(min = 1, max = 50, message = "1자 이상 50자 이하로 입력해주세요.")
    @NotBlank
    String content;
    LocalDateTime localDateTime;

    public NotificationDto(@Size(min = 1, max = 50, message = "1자 이상 50자 이하로 입력해주세요.") @NotBlank String content, LocalDateTime localDateTime) {
        this.content = content;
        this.localDateTime = localDateTime;
    }

    public static NotificationDtoBuilder builder() {
        return new NotificationDtoBuilder();
    }

    public @Size(min = 1, max = 50, message = "1자 이상 50자 이하로 입력해주세요.") @NotBlank String getContent() {
        return this.content;
    }

    public LocalDateTime getLocalDateTime() {
        return this.localDateTime;
    }

    public static class NotificationDtoBuilder {
        private @Size(min = 1, max = 50, message = "1자 이상 50자 이하로 입력해주세요.") @NotBlank String content;
        private LocalDateTime localDateTime;

        NotificationDtoBuilder() {
        }

        public NotificationDtoBuilder content(@Size(min = 1, max = 50, message = "1자 이상 50자 이하로 입력해주세요.") @NotBlank String content) {
            this.content = content;
            return this;
        }

        public NotificationDtoBuilder localDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
            return this;
        }

        public NotificationDto build() {
            return new NotificationDto(this.content, this.localDateTime);
        }

        public String toString() {
            return "NotificationDto.NotificationDtoBuilder(content=" + this.content + ", localDateTime=" + this.localDateTime + ")";
        }
    }
}
