package org.mint.smallcloud.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService userDetailsService;
    private final UserService userService;
    private final UserDetailsProvider userDetailsProvider;

    public JwtTokenDto login(LoginDto loginDto) {
        userService.checkPassword(loginDto);
        return jwtTokenProvider.generateTokenDto(userDetailsService.loadUserByUsername(loginDto.getId()));
    }

    public String refresh(String refreshToken) {
        Date now = new Date();
        jwtTokenProvider.validateToken(refreshToken);
        return jwtTokenProvider.generateAccessTokenFromRefreshToken(refreshToken, now);
    }

    public JwtTokenDto elevate(String password) {
        String userId = userDetailsProvider.getUserId()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_VALID_JWT_TOKEN));
        userService.checkPassword(new LoginDto(userId, password));
        UserDetails user = userDetailsService.loadUserByUsername(userId);
        user = userDetailsService.getElevateUser(user);
        return jwtTokenProvider.generateTokenDto(user);
    }

    public void deregister() {
        String userId = userDetailsProvider.getUserId().orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_VALID_JWT_TOKEN));
        userService.deregisterUser(userId);
    }
}
