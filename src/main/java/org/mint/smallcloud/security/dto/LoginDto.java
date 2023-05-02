package org.mint.smallcloud.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginDto {
    private String id;
    private String password;
}
