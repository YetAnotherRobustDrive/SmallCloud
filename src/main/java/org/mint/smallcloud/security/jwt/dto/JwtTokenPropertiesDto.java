package org.mint.smallcloud.security.jwt.dto;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;

import java.security.Key;

@Getter
@Builder
public class JwtTokenPropertiesDto {
    private final String roleField;
    private final String tokenTypeHeader;
    private final String grantType;
    private final Key key;
    private final SignatureAlgorithm signatureAlgorithm;
}
