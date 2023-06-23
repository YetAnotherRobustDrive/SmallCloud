package org.mint.smallcloud.security.jwt.tokenprovider.token;

import java.util.Optional;

public enum JwtTokenType {
    ACCESS("ACCESS"), REFRESH("REFRESH");
    private String value;

    JwtTokenType(String value) {
        this.value = value;
    }

    public static Optional<JwtTokenType> of(String value) {
        switch (value) {
            case "ACCESS":
                return Optional.of(ACCESS);
            case "REFRESH":
                return Optional.of(REFRESH);
            default:
                return Optional.empty();
        }
    }

    public String getValue() {
        return this.value;
    }
}
