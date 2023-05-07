package org.mint.smallcloud.security.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.dto.RegisterDto;
import org.mint.smallcloud.security.jwt.JwtTokenDto;
import org.mint.smallcloud.security.jwt.JwtTokenProvider;
import org.mint.smallcloud.security.jwt.JwtUserService;
import org.mint.smallcloud.user.Roles;
import org.mint.smallcloud.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final JwtUserService jwtUserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserDetailsProvider userDetailsProvider;

    @PostMapping("/login")
    public JwtTokenDto login(@RequestBody LoginDto loginDto) {
        return jwtUserService.login(loginDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody RegisterDto registerDto) {
        userService.registerUser(registerDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh")
    public String refresh(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveTokenFromHeader(request);
        return jwtUserService.refresh(refreshToken);
    }

    @Secured({Roles.S_COMMON, Roles.S_PRIVILEGE})
    @PostMapping("/elevate")
    public JwtTokenDto elevate(@RequestBody PasswordDto passwordDto) {
        return jwtUserService.elevate(passwordDto.getPassword());
    }

    @Secured({Roles.S_PRIVILEGE})
    @PostMapping("/deregister")
    public void deregister() {
        jwtUserService.deregister();
    }

    @GetMapping("/logout")
    public void logout() {
        /* TODO: 추후 구현 고려 필수 사항은 아닌 거 같음 */
    }

    @Getter
    private static class PasswordDto {
        public String password;
    }
}
