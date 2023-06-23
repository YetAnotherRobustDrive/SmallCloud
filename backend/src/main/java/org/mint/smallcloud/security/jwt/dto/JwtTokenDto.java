package org.mint.smallcloud.security.jwt.dto;

public class JwtTokenDto {
    private final String grantType;
    private final String accessToken;
    private final String refreshToken;

    public JwtTokenDto(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static JwtTokenDtoBuilder builder() {
        return new JwtTokenDtoBuilder();
    }

    public String getGrantType() {
        return this.grantType;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public static class JwtTokenDtoBuilder {
        private String grantType;
        private String accessToken;
        private String refreshToken;

        JwtTokenDtoBuilder() {
        }

        public JwtTokenDtoBuilder grantType(String grantType) {
            this.grantType = grantType;
            return this;
        }

        public JwtTokenDtoBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public JwtTokenDtoBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public JwtTokenDto build() {
            return new JwtTokenDto(this.grantType, this.accessToken, this.refreshToken);
        }

        public String toString() {
            return "JwtTokenDto.JwtTokenDtoBuilder(grantType=" + this.grantType + ", accessToken=" + this.accessToken + ", refreshToken=" + this.refreshToken + ")";
        }
    }
}
