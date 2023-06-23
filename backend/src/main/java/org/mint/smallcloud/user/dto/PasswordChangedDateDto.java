package org.mint.smallcloud.user.dto;

import java.time.LocalDateTime;

public class PasswordChangedDateDto {
    private final LocalDateTime changedPasswordDate;

    public PasswordChangedDateDto(LocalDateTime changedPasswordDate) {
        this.changedPasswordDate = changedPasswordDate;
    }

    public static PasswordChangedDateDtoBuilder builder() {
        return new PasswordChangedDateDtoBuilder();
    }

    public LocalDateTime getChangedPasswordDate() {
        return this.changedPasswordDate;
    }

    public static class PasswordChangedDateDtoBuilder {
        private LocalDateTime changedPasswordDate;

        PasswordChangedDateDtoBuilder() {
        }

        public PasswordChangedDateDtoBuilder changedPasswordDate(LocalDateTime changedPasswordDate) {
            this.changedPasswordDate = changedPasswordDate;
            return this;
        }

        public PasswordChangedDateDto build() {
            return new PasswordChangedDateDto(this.changedPasswordDate);
        }

        public String toString() {
            return "PasswordChangedDateDto.PasswordChangedDateDtoBuilder(changedPasswordDate=" + this.changedPasswordDate + ")";
        }
    }
}
