package org.mint.smallcloud.user.dto;

import org.mint.smallcloud.validation.NicknameValidation;
import org.mint.smallcloud.validation.UserNameValidation;

public class UserProfileRequestDto {
    @UserNameValidation
    private String username;
    @NicknameValidation
    private String nickname;

    public UserProfileRequestDto(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }

    public static UserProfileRequestDtoBuilder builder() {
        return new UserProfileRequestDtoBuilder();
    }

    public String getUsername() {
        return this.username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public static class UserProfileRequestDtoBuilder {
        private String username;
        private String nickname;

        UserProfileRequestDtoBuilder() {
        }

        public UserProfileRequestDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserProfileRequestDtoBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UserProfileRequestDto build() {
            return new UserProfileRequestDto(this.username, this.nickname);
        }

        public String toString() {
            return "UserProfileRequestDto.UserProfileRequestDtoBuilder(username=" + this.username + ", nickname=" + this.nickname + ")";
        }
    }
}
