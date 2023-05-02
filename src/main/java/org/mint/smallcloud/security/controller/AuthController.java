package org.mint.smallcloud.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.security.dto.RegisterDto;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.jwt.JwtTokenDto;
import org.mint.smallcloud.security.jwt.JwtUserService;
import org.mint.smallcloud.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final JwtUserService jwtUserService;
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
    public JwtTokenDto refresh(HttpServletRequest request) {
        /* TODO */
        return JwtTokenDto.builder()
                .grantType("")
                .accessToken("")
                .refreshToken("")
                .build();
    }

    /* for test */
    @GetMapping("/user")
    public ResponseEntity<?> user() {
        return ResponseEntity.ok(userDetailsProvider.getUserId());
    }
}
