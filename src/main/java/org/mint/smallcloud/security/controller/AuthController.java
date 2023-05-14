package org.mint.smallcloud.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.ResponseDto;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.service.AuthFacadeService;
import org.mint.smallcloud.user.domain.Roles;
import org.mint.smallcloud.user.dto.RegisterDto;
import org.mint.smallcloud.validation.PasswordValidation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthFacadeService authFacadeService;

    @PostMapping("/login")
    public JwtTokenDto login(@Valid @RequestBody LoginDto loginDto) {
        return authFacadeService.login(loginDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterDto registerDto) {
        authFacadeService.signup(registerDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh")
    public ResponseDto<String> refresh(HttpServletRequest request) {
        String result = authFacadeService.refresh(request);
        return ResponseDto.<String>builder().result(result).build();
    }

    @Secured({Roles.S_COMMON, Roles.S_PRIVILEGE})
    @PostMapping("/elevate")
    public JwtTokenDto elevate(@Valid @RequestBody PasswordDto passwordDto) {
        return authFacadeService.elevate(passwordDto.getPassword());
    }

    @Secured({Roles.S_PRIVILEGE})
    @PostMapping("/deregister")
    public void deregister() {
        authFacadeService.deregister();
    }

    @GetMapping("/privileged")
    public ResponseDto<Boolean> privileged() {
        boolean result = authFacadeService.privileged();
        return ResponseDto.<Boolean>builder().result(result).build();
    }

    @GetMapping("/logout")
    public void logout() {
        /* TODO: 추후 구현 고려 필수 사항은 아닌 거 같음 */
    }

    private static class PasswordDto {
        @PasswordValidation
        private String password;

        public String getPassword() {
            return password;
        }
    }
}
