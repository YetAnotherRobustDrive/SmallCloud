package org.mint.smallcloud.user.dto;


import java.time.LocalDateTime;

public class UpdateExpiredDto {
    private final String username;
    private final LocalDateTime expiredDate;

    public UpdateExpiredDto(String username, LocalDateTime expiredDate) {
        this.username = username;
        this.expiredDate = expiredDate;
    }

    public static UpdateExpiredDtoBuilder builder() {
        return new UpdateExpiredDtoBuilder();
    }

    public String getUsername() {
        return this.username;
    }

    public LocalDateTime getExpiredDate() {
        return this.expiredDate;
    }

    public static class UpdateExpiredDtoBuilder {
        private String username;
        private LocalDateTime expiredDate;

        UpdateExpiredDtoBuilder() {
        }

        public UpdateExpiredDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UpdateExpiredDtoBuilder expiredDate(LocalDateTime expiredDate) {
            this.expiredDate = expiredDate;
            return this;
        }

        public UpdateExpiredDto build() {
            return new UpdateExpiredDto(this.username, this.expiredDate);
        }

        public String toString() {
            return "UpdateExpiredDto.UpdateExpiredDtoBuilder(username=" + this.username + ", expiredDate=" + this.expiredDate + ")";
        }
    }
}
