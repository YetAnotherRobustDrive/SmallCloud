package org.mint.smallcloud.user.dto;

import org.mint.smallcloud.validation.BlankMessages;
import org.mint.smallcloud.validation.NicknameValidation;
import org.mint.smallcloud.validation.PasswordValidation;
import org.mint.smallcloud.validation.UserNameValidation;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class RegisterFromAdminDto {
    @NicknameValidation
    @NotBlank(message = BlankMessages.NICKNAME)
    private final String name;

    @UserNameValidation
    @NotBlank(message = BlankMessages.USER_NAME)
    private final String id;

    @PasswordValidation
    @NotBlank(message = BlankMessages.PASSWORD)
    private final String password;

    private final LocalDateTime expiredDate;

    public RegisterFromAdminDto(@NotBlank(message = BlankMessages.NICKNAME) String name, @NotBlank(message = BlankMessages.USER_NAME) String id, @NotBlank(message = BlankMessages.PASSWORD) String password, LocalDateTime expiredDate) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.expiredDate = expiredDate;
    }

    public static RegisterFromAdminDtoBuilder builder() {
        return new RegisterFromAdminDtoBuilder();
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

    public LocalDateTime getExpiredDate() {
        return this.expiredDate;
    }

    public static class RegisterFromAdminDtoBuilder {
        private @NotBlank(message = BlankMessages.NICKNAME) String name;
        private @NotBlank(message = BlankMessages.USER_NAME) String id;
        private @NotBlank(message = BlankMessages.PASSWORD) String password;
        private LocalDateTime expiredDate;

        RegisterFromAdminDtoBuilder() {
        }

        public RegisterFromAdminDtoBuilder name(@NotBlank(message = BlankMessages.NICKNAME) String name) {
            this.name = name;
            return this;
        }

        public RegisterFromAdminDtoBuilder id(@NotBlank(message = BlankMessages.USER_NAME) String id) {
            this.id = id;
            return this;
        }

        public RegisterFromAdminDtoBuilder password(@NotBlank(message = BlankMessages.PASSWORD) String password) {
            this.password = password;
            return this;
        }

        public RegisterFromAdminDtoBuilder expiredDate(LocalDateTime expiredDate) {
            this.expiredDate = expiredDate;
            return this;
        }

        public RegisterFromAdminDto build() {
            return new RegisterFromAdminDto(this.name, this.id, this.password, this.expiredDate);
        }

        public String toString() {
            return "RegisterFromAdminDto.RegisterFromAdminDtoBuilder(name=" + this.name + ", id=" + this.id + ", password=" + this.password + ", expiredDate=" + this.expiredDate + ")";
        }
    }
}
