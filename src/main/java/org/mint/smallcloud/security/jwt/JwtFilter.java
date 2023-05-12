package org.mint.smallcloud.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.FilterExceptionManager;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt token의 인증을 위한 filter 한 번 실행
 */
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsProvider userDetailsProvider;
    private final FilterExceptionManager filterExceptionManager;
    private final Logger loggger = LoggerFactory.getLogger("test");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = tokenProvider.resolveTokenFromHeader(request);
            if (StringUtils.hasText(jwt)) {
                tokenProvider.validateAccessToken(jwt);
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                userDetailsProvider.setAuthentication(authentication);
            }
        } catch (ServiceException exception) {
            filterExceptionManager.addException(request, exception);
        }
        filterChain.doFilter(request, response);
    }
}
