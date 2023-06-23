package org.mint.smallcloud.user.dto;

import org.mint.smallcloud.validation.BlankMessages;
import org.mint.smallcloud.validation.NicknameValidation;
import org.mint.smallcloud.validation.PasswordValidation;
import org.mint.smallcloud.validation.UserNameValidation;

import javax.validation.constraints.NotBlank;

public class RegisterDto {

    @NicknameValidation
    @NotBlank(message = BlankMessages.NICKNAME)
    private final String name;

    @UserNameValidation
    @NotBlank(message = BlankMessages.USER_NAME)
    private final String id;

    @PasswordValidation
    @NotBlank(message = BlankMessages.PASSWORD)
    private final String password;

    public RegisterDto(@NotBlank(message = BlankMessages.NICKNAME) String name, @NotBlank(message = BlankMessages.USER_NAME) String id, @NotBlank(message = BlankMessages.PASSWORD) String password) {
        this.name = name;
        this.id = id;
        this.password = password;
    }

    public static RegisterDtoBuilder builder() {
        return new RegisterDtoBuilder();
    }

    public @NotBlank(message = BlankMessages.NICKNAME) String getName() {
        return this.name;
    }

    public @NotBlank(message = BlankMessages.USER_NAME) String getId() {
        return this.id;
    }

    public @NotBlank(message = BlankMessages.PASSWORD) String getPassword() {
        return this.password;
    }

    public static class RegisterDtoBuilder {
        private @NotBlank(message = BlankMessages.NICKNAME) String name;
        private @NotBlank(message = BlankMessages.USER_NAME) String id;
        private @NotBlank(message = BlankMessages.PASSWORD) String password;

        RegisterDtoBuilder() {
        }

        public RegisterDtoBuilder name(@NotBlank(message = BlankMessages.NICKNAME) String name) {
            this.name = name;
            return this;
        }

        public RegisterDtoBuilder id(@NotBlank(message = BlankMessages.USER_NAME) String id) {
            this.id = id;
            return this;
        }

        public RegisterDtoBuilder password(@NotBlank(message = BlankMessages.PASSWORD) String password) {
            this.password = password;
            return this;
        }

        public RegisterDto build() {
            return new RegisterDto(this.name, this.id, this.password);
        }

        public String toString() {
            return "RegisterDto.RegisterDtoBuilder(name=" + this.name + ", id=" + this.id + ", password=" + this.password + ")";
        }
    }
}
