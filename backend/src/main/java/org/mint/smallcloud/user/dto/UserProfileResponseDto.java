package org.mint.smallcloud.user.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.user.domain.Role;

import java.time.LocalDateTime;

public class UserProfileResponseDto {
    private String username;
    private String nickname;
    private LocalDateTime joinedDate;
    private LocalDateTime changedPasswordDate;
    private LocalDateTime expiredDate;
    private boolean locked;
    @JsonUnwrapped
    private FileLocation profileImageLocation;
    private Role role;
    private String groupName;

    public UserProfileResponseDto(String username, String nickname, LocalDateTime joinedDate, LocalDateTime changedPasswordDate, LocalDateTime expiredDate, boolean locked, FileLocation profileImageLocation, Role role, String groupName) {
        this.username = username;
        this.nickname = nickname;
        this.joinedDate = joinedDate;
        this.changedPasswordDate = changedPasswordDate;
        this.expiredDate = expiredDate;
        this.locked = locked;
        this.profileImageLocation = profileImageLocation;
        this.role = role;
        this.groupName = groupName;
    }

    public static UserProfileResponseDtoBuilder builder() {
        return new UserProfileResponseDtoBuilder();
    }

    public String getUsername() {
        return this.username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public LocalDateTime getJoinedDate() {
        return this.joinedDate;
    }

    public LocalDateTime getChangedPasswordDate() {
        return this.changedPasswordDate;
    }

    public LocalDateTime getExpiredDate() {
        return this.expiredDate;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public FileLocation getProfileImageLocation() {
        return this.profileImageLocation;
    }

    public Role getRole() {
        return this.role;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public static class UserProfileResponseDtoBuilder {
        private String username;
        private String nickname;
        private LocalDateTime joinedDate;
        private LocalDateTime changedPasswordDate;
        private LocalDateTime expiredDate;
        private boolean locked;
        private FileLocation profileImageLocation;
        private Role role;
        private String groupName;

        UserProfileResponseDtoBuilder() {
        }

        public UserProfileResponseDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserProfileResponseDtoBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UserProfileResponseDtoBuilder joinedDate(LocalDateTime joinedDate) {
            this.joinedDate = joinedDate;
            return this;
        }

        public UserProfileResponseDtoBuilder changedPasswordDate(LocalDateTime changedPasswordDate) {
            this.changedPasswordDate = changedPasswordDate;
            return this;
        }

        public UserProfileResponseDtoBuilder expiredDate(LocalDateTime expiredDate) {
            this.expiredDate = expiredDate;
            return this;
        }

        public UserProfileResponseDtoBuilder locked(boolean locked) {
            this.locked = locked;
            return this;
        }

        @JsonUnwrapped
        public UserProfileResponseDtoBuilder profileImageLocation(FileLocation profileImageLocation) {
            this.profileImageLocation = profileImageLocation;
            return this;
        }

        public UserProfileResponseDtoBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserProfileResponseDtoBuilder groupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public UserProfileResponseDto build() {
            return new UserProfileResponseDto(this.username, this.nickname, this.joinedDate, this.changedPasswordDate, this.expiredDate, this.locked, this.profileImageLocation, this.role, this.groupName);
        }

        public String toString() {
            return "UserProfileResponseDto.UserProfileResponseDtoBuilder(username=" + this.username + ", nickname=" + this.nickname + ", joinedDate=" + this.joinedDate + ", changedPasswordDate=" + this.changedPasswordDate + ", expiredDate=" + this.expiredDate + ", locked=" + this.locked + ", profileImageLocation=" + this.profileImageLocation + ", role=" + this.role + ", groupName=" + this.groupName + ")";
        }
    }
}
