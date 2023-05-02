package org.mint.smallcloud.user;

import lombok.Getter;

@Getter
public enum Roles {
    ADMIN("ADMIN"),
    COMMON("COMMON");

    private final String role;
    Roles(String role) {
        this.role = role;
    }
}
