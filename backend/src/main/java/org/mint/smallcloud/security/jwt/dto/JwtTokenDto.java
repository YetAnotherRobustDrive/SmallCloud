package org.mint.smallcloud.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
public class JwtTokenDto {
    private final String grantType;
    private final String accessToken;
    private final String refreshToken;
}
