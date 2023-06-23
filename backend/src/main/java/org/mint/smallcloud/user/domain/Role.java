package org.mint.smallcloud.user.domain;

import java.util.Optional;


public enum Role {
    ADMIN(Roles.ADMIN),
    COMMON(Roles.COMMON),
    PRIVILEGE(Roles.PRIVILEGE);

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public static Optional<Role> of(String value) {
        switch (value) {
            case Roles.ADMIN:
                return Optional.of(ADMIN);
            case Roles.COMMON:
                return Optional.of(COMMON);
            case Roles.PRIVILEGE:
                return Optional.of(PRIVILEGE);
        }
        return Optional.empty();
    }

    public String getValue() {
        return this.value;
    }
}
