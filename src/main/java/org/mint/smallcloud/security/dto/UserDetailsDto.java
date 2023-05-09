package org.mint.smallcloud.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.user.domain.Role;

@AllArgsConstructor
@Builder
@Getter
public class UserDetailsDto {
    private final String username;
    private final String password;
    private final Role roles;
    private final boolean disabled;
}
