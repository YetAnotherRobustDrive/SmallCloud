package org.mint.smallcloud.security.jwt.dto;

import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;

public class JwtTokenPropertiesDto {
    private final String roleField;
    private final String tokenTypeHeader;
    private final String grantType;
    private final Key key;
    private final SignatureAlgorithm signatureAlgorithm;

    JwtTokenPropertiesDto(String roleField, String tokenTypeHeader, String grantType, Key key, SignatureAlgorithm signatureAlgorithm) {
        this.roleField = roleField;
        this.tokenTypeHeader = tokenTypeHeader;
        this.grantType = grantType;
        this.key = key;
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public static JwtTokenPropertiesDtoBuilder builder() {
        return new JwtTokenPropertiesDtoBuilder();
    }

    public String getRoleField() {
        return this.roleField;
    }

    public String getTokenTypeHeader() {
        return this.tokenTypeHeader;
    }

    public String getGrantType() {
        return this.grantType;
    }

    public Key getKey() {
        return this.key;
    }

    public SignatureAlgorithm getSignatureAlgorithm() {
        return this.signatureAlgorithm;
    }

    public static class JwtTokenPropertiesDtoBuilder {
        private String roleField;
        private String tokenTypeHeader;
        private String grantType;
        private Key key;
        private SignatureAlgorithm signatureAlgorithm;

        JwtTokenPropertiesDtoBuilder() {
        }

        public JwtTokenPropertiesDtoBuilder roleField(String roleField) {
            this.roleField = roleField;
            return this;
        }

        public JwtTokenPropertiesDtoBuilder tokenTypeHeader(String tokenTypeHeader) {
            this.tokenTypeHeader = tokenTypeHeader;
            return this;
        }

        public JwtTokenPropertiesDtoBuilder grantType(String grantType) {
            this.grantType = grantType;
            return this;
        }

        public JwtTokenPropertiesDtoBuilder key(Key key) {
            this.key = key;
            return this;
        }

        public JwtTokenPropertiesDtoBuilder signatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
            this.signatureAlgorithm = signatureAlgorithm;
            return this;
        }

        public JwtTokenPropertiesDto build() {
            return new JwtTokenPropertiesDto(this.roleField, this.tokenTypeHeader, this.grantType, this.key, this.signatureAlgorithm);
        }

        public String toString() {
            return "JwtTokenPropertiesDto.JwtTokenPropertiesDtoBuilder(roleField=" + this.roleField + ", tokenTypeHeader=" + this.tokenTypeHeader + ", grantType=" + this.grantType + ", key=" + this.key + ", signatureAlgorithm=" + this.signatureAlgorithm + ")";
        }
    }
}
