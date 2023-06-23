package org.mint.smallcloud.security.jwt;

import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.security.mapper.UserDetailsResolver;
import org.mint.smallcloud.user.domain.Role;
import org.mint.smallcloud.user.service.MemberService;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUserService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JwtUserService.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService userDetailsService;
    private final MemberService memberService;
    private final UserDetailsResolver userdetailsResolver;

    public JwtUserService(JwtTokenProvider jwtTokenProvider, JwtUserDetailsService userDetailsService, MemberService memberService, UserDetailsResolver userdetailsResolver) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.memberService = memberService;
        this.userdetailsResolver = userdetailsResolver;
    }

    public JwtTokenDto login(LoginDto loginDto) {
        memberService.validLogin(loginDto);
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
        memberService.checkPassword(new LoginDto(user.getUsername(), password));
        UserDetailsDto userDetailsDto = userdetailsResolver.toUserDetailsDto(user);
        return jwtTokenProvider.elevateJwtToken(userDetailsDto, Role.PRIVILEGE);
    }

    public boolean isElevated(UserDetails user) {
        return userdetailsResolver.isRole(user, Role.PRIVILEGE);
    }

    public boolean isAdmin(UserDetails user) {
        return userdetailsResolver.isRole(user, Role.ADMIN);
    }
}
