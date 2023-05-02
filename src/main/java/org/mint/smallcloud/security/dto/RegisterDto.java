package org.mint.smallcloud.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RegisterDto {
    private final String name;
    private final String id;
    private final String password;
}
