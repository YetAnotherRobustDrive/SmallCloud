package org.mint.smallcloud.user.dto;

import org.mint.smallcloud.validation.NicknameValidation;
import org.mint.smallcloud.validation.UserNameValidation;

public class UserLabelDto {
    @UserNameValidation
    private String username;
    @NicknameValidation
    private String nickname;

    public UserLabelDto(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }

    public static UserLabelDtoBuilder builder() {
        return new UserLabelDtoBuilder();
    }

    public String getUsername() {
        return this.username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public static class UserLabelDtoBuilder {
        private String username;
        private String nickname;

        UserLabelDtoBuilder() {
        }

        public UserLabelDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserLabelDtoBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UserLabelDto build() {
            return new UserLabelDto(this.username, this.nickname);
        }

        public String toString() {
            return "UserLabelDto.UserLabelDtoBuilder(username=" + this.username + ", nickname=" + this.nickname + ")";
        }
    }
}
