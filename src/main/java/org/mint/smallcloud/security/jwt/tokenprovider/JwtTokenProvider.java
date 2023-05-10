package org.mint.smallcloud.security.jwt.tokenprovider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.JwtUserDetailsService;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.token.JwtToken;
import org.mint.smallcloud.security.jwt.tokenprovider.token.JwtTokenType;
import org.mint.smallcloud.security.mapper.UserDetailsResolver;
import org.mint.smallcloud.user.domain.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final JwtUserDetailsService userDetailsService;
    private final JwtTokenProperties tokenProperties;
    private final JwtTokenFactory tokenFactory;
    private final JwtTokenParser jwtTokenParser;
    private final UserDetailsResolver userDetailsResolver;

    public JwtTokenDto generateTokenDto(UserDetailsDto userDetailsDto) {
        LocalDateTime now = LocalDateTime.now();
        JwtToken token = tokenFactory.createWithRole(
            userDetailsDto.getUsername(),
            now, userDetailsDto.getRoles());
        return token.toDto();
    }

    public String resolveTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request
            .getHeader(tokenProperties.getAuthorizationHeader());
        if (StringUtils.hasText(bearerToken)
            && bearerToken.startsWith(tokenProperties.getGrantTypePrefix())) {
            return bearerToken.substring(tokenProperties.getGrantTypePrefix().length());
        }
        throw new ServiceException(ExceptionStatus.NOT_FOUND_JWT_TOKEN);
    }

    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsernameAndRole(
            jwtTokenParser.getSubject(accessToken),
            jwtTokenParser.getRole(accessToken)
        );
        log.info("{}", jwtTokenParser.getRole(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public void validateToken(String token) {
        jwtTokenParser.validate(token);
    }

    public void validateAccessToken(String token) {
        validateToken(token);
        if (!jwtTokenParser.getTokenType(token).equals(JwtTokenType.ACCESS))
            throw new ServiceException(ExceptionStatus.EXPIRED_JWT_TOKEN);
    }

    public String generateNewAccessToken(String refreshToken) {
        LocalDateTime now = LocalDateTime.now();
        if (!jwtTokenParser.isTokenType(refreshToken, JwtTokenType.REFRESH))
            throw new ServiceException(ExceptionStatus.NOT_REFRESH_TOKEN);
        String userId = jwtTokenParser.getSubject(refreshToken);
        Role role = jwtTokenParser.getRole(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(userId);
        if (!userId.equals(user.getUsername()))
            throw new ServiceException(ExceptionStatus.NOT_VALID_JWT_TOKEN);
        user = userDetailsService.getElevatedUser(user, role);
        return tokenFactory
            .createWithRole(user.getUsername(), now, role)
            .toDto().getAccessToken();
    }

    public JwtTokenDto elevateJwtToken(UserDetailsDto userDto, Role role) {
        LocalDateTime now = LocalDateTime.now();
        UserDetails userDetails = userDetailsService.loadUserByUsernameAndRole(userDto.getUsername(), role);
        return tokenFactory.createWithRole(
            userDetails.getUsername(), now,
            userDetailsResolver.getRole(userDetails)
                .orElseThrow(IllegalArgumentException::new)).toDto();
    }
}