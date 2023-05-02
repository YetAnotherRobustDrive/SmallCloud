package org.mint.smallcloud.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.security.dto.LoginDto;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService userDetailsService;

    public JwtTokenDto login(LoginDto loginDto) {
        return jwtTokenProvider.generateTokenDto(userDetailsService.loadUserByUsername(loginDto.getId()));
    }
}
