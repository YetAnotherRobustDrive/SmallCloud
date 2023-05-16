package org.mint.smallcloud.security.service;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.jwt.JwtUserService;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.user.dto.RegisterDto;
import org.mint.smallcloud.user.service.MemberService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AuthFacadeService {
    private final JwtUserService jwtUserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final AuthThrowerService authThrowerService;

    public JwtTokenDto login(LoginDto loginDto) {
        return jwtUserService.login(loginDto);
    }

    public void signup(RegisterDto registerDto) {
        memberService.registerCommon(registerDto);
    }

    public String refresh(HttpServletRequest request) {
        String refreshToken =
            jwtTokenProvider.resolveTokenFromHeader(request);
        return jwtUserService.refresh(refreshToken);
    }

    public JwtTokenDto elevate(String password) {
        return jwtUserService.elevate(
            authThrowerService.getLoginUserDetails(),
            password);
    }

    public void deregister() {
        memberService.deregisterCommon(
            authThrowerService.getLoginUserDetails().getUsername()
        );
    }

    public boolean privileged() {
        return jwtUserService.isElevated(
            authThrowerService.getLoginUserDetails());
    }

    public boolean isadmin() {
        return jwtUserService.isAdmin(
            authThrowerService.getLoginUserDetails());
    }
}
