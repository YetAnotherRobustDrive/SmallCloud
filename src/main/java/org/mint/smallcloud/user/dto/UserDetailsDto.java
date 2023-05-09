package org.mint.smallcloud.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserDetailsDto {
    private final String username;
    private final String password;
    private final String roles;
    private final boolean disabled;
}
