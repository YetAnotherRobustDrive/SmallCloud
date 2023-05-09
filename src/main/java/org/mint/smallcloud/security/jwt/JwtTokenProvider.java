package org.mint.smallcloud.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";
    private static final String BEARER_PREFIX = BEARER_TYPE + " ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final Key key;

    private final JwtUserDetailsService userDetailsService;

    public JwtTokenProvider(
        @Value("${jwt.secret}") String secretKey,
        JwtUserDetailsService userDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
    }

    public JwtTokenDto generateTokenDto(UserDetails user) {
        // 권한들 가져오기
        Date now = new Date();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
            .setSubject(user.getUsername())
            .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        String accessToken = Jwts.builder()
            .setSubject(user.getUsername())       // payload "sub": "name"
            .claim(AUTHORITIES_KEY, marshalAuth(user.getAuthorities()))        // payload "auth": "ROLE_USER"
            .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))        // payload "exp": 1516239022 (예시)
            .signWith(key, SignatureAlgorithm.HS256)    // header "alg": "HS256"
            .compact();

        return JwtTokenDto.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public String generateAccessTokenFromRefreshToken(String refreshToken, Date now) {
        validateToken(refreshToken);
        String userId = getSubject(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(userId);
        if (!userId.equals(user.getUsername()))
            throw new ServiceException(ExceptionStatus.NOT_VALID_JWT_TOKEN);
        Date accessTokenExpiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        return Jwts.builder()
            .setSubject(userId)       // payload "sub": "name"
            .claim(AUTHORITIES_KEY, marshalAuth(user.getAuthorities()))        // payload "auth": "ROLE_USER"
            .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
            .signWith(key, SignatureAlgorithm.HS256)    // header "alg": "HS256"
            .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        String auth = (String) claims.get(AUTHORITIES_KEY);
        if (auth == null)
            throw new ServiceException(ExceptionStatus.NOT_VALID_JWT_TOKEN);
        List<? extends GrantedAuthority> grantedAuthorities = unmarshalAuth(auth);
        UserDetails userDetails = userDetailsService.loadUserByUsername(getSubject(accessToken));
        for (GrantedAuthority authority : grantedAuthorities) {
            log.info("auth: {}", authority.getAuthority());
            if (authority.getAuthority().equals(Roles.S_PRIVILEGE))
                userDetails = userDetailsService.getElevateUser(userDetails);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public void validateToken(String token) {
        log.info("token: {}", token);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException
                 | MalformedJwtException | UnsupportedJwtException
                 | IllegalArgumentException e) {
            throw new ServiceException(ExceptionStatus.NOT_VALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new ServiceException(ExceptionStatus.EXPIRED_JWT_TOKEN);
        }
    }

    public String resolveTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken)
            && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private String getSubject(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    private String marshalAuth(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    }

    private List<? extends GrantedAuthority> unmarshalAuth(String auth) {
        return Arrays.stream(auth.split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}
