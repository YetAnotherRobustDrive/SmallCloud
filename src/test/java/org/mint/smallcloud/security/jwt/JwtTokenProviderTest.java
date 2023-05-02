package org.mint.smallcloud.security.jwt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider
            = new JwtTokenProvider("SECRETkey", );

    @Test
    public void 토큰을_만든_것이_잘_작동하나() {
    }
}