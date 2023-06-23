package org.mint.smallcloud.security.dto;

import org.mint.smallcloud.user.domain.Role;

public class UserDetailsDto {
    private final String username;
    private final String password;
    private final Role roles;
    private final boolean disabled;

    public UserDetailsDto(String username, String password, Role roles, boolean disabled) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.disabled = disabled;
    }

    public static UserDetailsDtoBuilder builder() {
        return new UserDetailsDtoBuilder();
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Role getRoles() {
        return this.roles;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public static class UserDetailsDtoBuilder {
        private String username;
        private String password;
        private Role roles;
        private boolean disabled;

        UserDetailsDtoBuilder() {
        }

        public UserDetailsDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserDetailsDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserDetailsDtoBuilder roles(Role roles) {
            this.roles = roles;
            return this;
        }

        public UserDetailsDtoBuilder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public UserDetailsDto build() {
            return new UserDetailsDto(this.username, this.password, this.roles, this.disabled);
        }

        public String toString() {
            return "UserDetailsDto.UserDetailsDtoBuilder(username=" + this.username + ", password=" + this.password + ", roles=" + this.roles + ", disabled=" + this.disabled + ")";
        }
    }
}
