package org.mint.smallcloud.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.mint.smallcloud.validation.BlankMessages;
import org.mint.smallcloud.validation.PasswordValidation;

import javax.validation.constraints.NotBlank;

public class ChangePasswordDto {
    @PasswordValidation
    @NotBlank(message = BlankMessages.PASSWORD)
    private final String password;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ChangePasswordDto(@NotBlank(message = BlankMessages.PASSWORD) String password) {
        this.password = password;
    }

    public static ChangePasswordDtoBuilder builder() {
        return new ChangePasswordDtoBuilder();
    }

    public @NotBlank(message = BlankMessages.PASSWORD) String getPassword() {
        return this.password;
    }

    public static class ChangePasswordDtoBuilder {
        private @NotBlank(message = BlankMessages.PASSWORD) String password;

        ChangePasswordDtoBuilder() {
        }

        public ChangePasswordDtoBuilder password(@NotBlank(message = BlankMessages.PASSWORD) String password) {
            this.password = password;
            return this;
        }

        public ChangePasswordDto build() {
            return new ChangePasswordDto(this.password);
        }

        public String toString() {
            return "ChangePasswordDto.ChangePasswordDtoBuilder(password=" + this.password + ")";
        }
    }
}
