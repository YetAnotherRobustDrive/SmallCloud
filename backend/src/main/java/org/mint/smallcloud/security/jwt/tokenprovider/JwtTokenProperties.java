package org.mint.smallcloud.security.jwt.tokenprovider;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@Getter
class JwtTokenProperties {
    @Value("${jwt.secret}")
    private String secretKey;
    private final String authoritiesField = "auth";
    private final String authorizationHeader = HttpHeaders.AUTHORIZATION;
    private final String grantType = "Bearer";
    private final SignatureAlgorithm signaturealgorithm = SignatureAlgorithm.HS256;
    private final String roleField = "role";
    private final String typeHeaderField = "tokenType";
    private final String queryParam = "token";

    public Key getTokenKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    String getGrantTypePrefix() {
        return grantType + ' ';
    }
}
