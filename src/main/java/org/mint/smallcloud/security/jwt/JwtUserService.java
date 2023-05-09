package org.mint.smallcloud.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.security.mapper.UserDetailsResolver;
import org.mint.smallcloud.user.domain.Role;
import org.mint.smallcloud.user.service.MemberService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService userDetailsService;
    private final MemberService userService;
    private final UserDetailsResolver userdetailsResolver;

    public JwtTokenDto login(LoginDto loginDto) {
        userService.checkPassword(loginDto);
        return jwtTokenProvider.generateTokenDto(
            userdetailsResolver.toUserDetailsDto(
                userDetailsService.loadUserByUsername(loginDto.getId())
            ));
    }

    public String refresh(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        return jwtTokenProvider.generateNewAccessToken(refreshToken);
    }

    public JwtTokenDto elevate(UserDetails user, String password) {
        userService.checkPassword(new LoginDto(user.getUsername(), password));
        UserDetailsDto userDetailsDto = userdetailsResolver.toUserDetailsDto(user);
        return jwtTokenProvider.elevateJwtToken(userDetailsDto, Role.PRIVILEGE);
    }

    public void deregister(UserDetails user) {
        userService.deregisterCommon(user.getUsername());
    }
}
