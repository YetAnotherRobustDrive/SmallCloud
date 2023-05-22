package org.mint.smallcloud.security.jwt.tokenprovider.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.Getter;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenPropertiesDto;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Map;

@Getter(AccessLevel.PROTECTED)
public abstract class JwtToken {
    private String username;
    private LocalDateTime now;

    private String grantType;
    private String roleField;
    private String tokenTypeHeader;
    private Key key;
    private SignatureAlgorithm signatureAlgorithm;

    @Getter(AccessLevel.NONE)
    private boolean isPrepared = false;

    private String accessToken;
    private String refreshToken;
    private Map<String, Object> payload;

    public JwtTokenDto toDto() {
        checkPrepared();
        return JwtTokenDto.builder()
            .accessToken(getAccessToken())
            .refreshToken(getRefreshToken())
            .grantType(getGrantType())
            .build();
    }

    public JwtToken prepare(String username, LocalDateTime now, JwtTokenPropertiesDto tokenProperties, Map<String, Object> payload) {
        this.username = username;
        this.now = now;
        this.grantType = tokenProperties.getGrantType();
        this.roleField = tokenProperties.getRoleField();
        this.key = tokenProperties.getKey();
        this.tokenTypeHeader = tokenProperties.getTokenTypeHeader();
        this.signatureAlgorithm = tokenProperties.getSignatureAlgorithm();
        this.refreshToken = getRefreshToken();
        this.accessToken = getAccessToken();
        this.payload = payload;
        this.isPrepared = true;
        return this;
    }

    private void checkPrepared() {
        if (!this.isPrepared)
            throw new IllegalArgumentException("token 값이 설정되지 않았습니다.");
    }

    private String getAccessToken() {
        return Jwts.builder()
            .setSubject(username)
            .setHeaderParam(getTokenTypeHeader(), JwtTokenType.ACCESS.getValue())
            .claim(getRoleField(), getRole())
            .addClaims(this.payload)
            .setExpiration(
                java.sql.Timestamp.valueOf(
                    this.now.plusSeconds(getAccessTokenExpireSec()))
            )
            .signWith(getKey(), getSignatureAlgorithm())
            .compact();
    }

    private String getRefreshToken() {
        return Jwts.builder()
            .setSubject(username)
            .setHeaderParam(getTokenTypeHeader(), JwtTokenType.REFRESH.getValue())
            .claim(getRoleField(), getRole())
            .setExpiration(
                java.sql.Timestamp.valueOf(
                    this.now.plusSeconds(getRefreshTokenExpireSec()))
            )
            .signWith(getKey(), getSignatureAlgorithm())
            .compact();
    }

    protected abstract String getRole();

    protected abstract long getAccessTokenExpireSec();

    protected abstract long getRefreshTokenExpireSec();
}
