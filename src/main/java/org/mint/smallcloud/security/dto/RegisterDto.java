package org.mint.smallcloud.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class RegisterDto {
    private final String name;
    private final String id;
    private final String password;
}
