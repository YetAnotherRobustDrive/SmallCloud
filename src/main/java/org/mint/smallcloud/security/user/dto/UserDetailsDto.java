package org.mint.smallcloud.security.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetailsDto {
    private final String username;
    private final String password;
    private final String roles;
    private final boolean disabled;
}
