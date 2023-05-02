package org.mint.smallcloud.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterDto {
    private final String name;
    private final String id;
    private final String password;
}
