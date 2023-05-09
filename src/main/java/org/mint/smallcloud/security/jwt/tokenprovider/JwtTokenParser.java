package org.mint.smallcloud.security.jwt.tokenprovider;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.jwt.tokenprovider.token.JwtTokenType;
import org.mint.smallcloud.user.domain.Role;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenParser {
    private final JwtParser jwtParser;
    private final JwtTokenProperties jwtTokenProperties;

    public JwtTokenParser(JwtTokenProperties jwtTokenProperties) {
        this.jwtTokenProperties = jwtTokenProperties;
        this.jwtParser = Jwts.parserBuilder()
            .setSigningKey(jwtTokenProperties.getTokenKey())
            .build();
    }

    public String getSubject(String token) {
        return validate(token).getBody().getSubject();
    }

    public Role getRole(String token) {
        return Role.of(
                validate(token).getBody()
                    .get(jwtTokenProperties.getRoleField())
                    .toString())
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_VALID_JWT_TOKEN));
    }

    public JwtTokenType getTokenType(String token) {
        return JwtTokenType.of(validate(token).getHeader()
                .get(jwtTokenProperties.getTypeHeaderField())
                .toString())
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_VALID_JWT_TOKEN));
    }

    public boolean isTokenType(String token, JwtTokenType type) {
        return getTokenType(token).equals(type);
    }

    public Jws<Claims> validate(String token) {
        try {
            return jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ServiceException(ExceptionStatus.EXPIRED_JWT_TOKEN);
        } catch (Exception e) {
            throw new ServiceException(ExceptionStatus.NOT_VALID_JWT_TOKEN);
        }
    }
}
