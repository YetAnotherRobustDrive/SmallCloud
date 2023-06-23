package org.mint.smallcloud.user.dto;

import org.mint.smallcloud.validation.BlankMessages;
import org.mint.smallcloud.validation.PasswordValidation;

import javax.validation.constraints.NotBlank;

public class PasswordUpdateRequestDto {
    @PasswordValidation
    @NotBlank(message = BlankMessages.PASSWORD)
    private final String password;

    @PasswordValidation
    @NotBlank(message = BlankMessages.PASSWORD)
    private final String newPassword;

    public PasswordUpdateRequestDto(@NotBlank(message = BlankMessages.PASSWORD) String password, @NotBlank(message = BlankMessages.PASSWORD) String newPassword) {
        this.password = password;
        this.newPassword = newPassword;
    }

    public static PasswordUpdateRequestDtoBuilder builder() {
        return new PasswordUpdateRequestDtoBuilder();
    }

    public @NotBlank(message = BlankMessages.PASSWORD) String getPassword() {
        return this.password;
    }

    public @NotBlank(message = BlankMessages.PASSWORD) String getNewPassword() {
        return this.newPassword;
    }

    public static class PasswordUpdateRequestDtoBuilder {
        private @NotBlank(message = BlankMessages.PASSWORD) String password;
        private @NotBlank(message = BlankMessages.PASSWORD) String newPassword;

        PasswordUpdateRequestDtoBuilder() {
        }

        public PasswordUpdateRequestDtoBuilder password(@NotBlank(message = BlankMessages.PASSWORD) String password) {
            this.password = password;
            return this;
        }

        public PasswordUpdateRequestDtoBuilder newPassword(@NotBlank(message = BlankMessages.PASSWORD) String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public PasswordUpdateRequestDto build() {
            return new PasswordUpdateRequestDto(this.password, this.newPassword);
        }

        public String toString() {
            return "PasswordUpdateRequestDto.PasswordUpdateRequestDtoBuilder(password=" + this.password + ", newPassword=" + this.newPassword + ")";
        }
    }
}
