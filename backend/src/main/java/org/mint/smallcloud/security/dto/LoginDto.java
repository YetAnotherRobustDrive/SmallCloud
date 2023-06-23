package org.mint.smallcloud.security.dto;

import org.mint.smallcloud.validation.BlankMessages;
import org.mint.smallcloud.validation.PasswordValidation;
import org.mint.smallcloud.validation.UserNameValidation;

import javax.validation.constraints.NotBlank;

public class LoginDto {
    @UserNameValidation
    @NotBlank(message = BlankMessages.USER_NAME)
    private final String id;

    @PasswordValidation
    @NotBlank(message = BlankMessages.PASSWORD)
    private final String password;

    public LoginDto(@NotBlank(message = BlankMessages.USER_NAME) String id, @NotBlank(message = BlankMessages.PASSWORD) String password) {
        this.id = id;
        this.password = password;
    }

    public static LoginDtoBuilder builder() {
        return new LoginDtoBuilder();
    }

    public @NotBlank(message = BlankMessages.USER_NAME) String getId() {
        return this.id;
    }

    public @NotBlank(message = BlankMessages.PASSWORD) String getPassword() {
        return this.password;
    }

    public static class LoginDtoBuilder {
        private @NotBlank(message = BlankMessages.USER_NAME) String id;
        private @NotBlank(message = BlankMessages.PASSWORD) String password;

        LoginDtoBuilder() {
        }

        public LoginDtoBuilder id(@NotBlank(message = BlankMessages.USER_NAME) String id) {
            this.id = id;
            return this;
        }

        public LoginDtoBuilder password(@NotBlank(message = BlankMessages.PASSWORD) String password) {
            this.password = password;
            return this;
        }

        public LoginDto build() {
            return new LoginDto(this.id, this.password);
        }

        public String toString() {
            return "LoginDto.LoginDtoBuilder(id=" + this.id + ", password=" + this.password + ")";
        }
    }
}
