package org.mint.smallcloud.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JwtTokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
