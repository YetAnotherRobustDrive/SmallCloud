package org.mint.smallcloud.security.jwt.tokenprovider;

import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.jwt.dto.JwtTokenPropertiesDto;
import org.mint.smallcloud.security.jwt.tokenprovider.token.AdminJwtToken;
import org.mint.smallcloud.security.jwt.tokenprovider.token.CommonJwtToken;
import org.mint.smallcloud.security.jwt.tokenprovider.token.JwtToken;
import org.mint.smallcloud.security.jwt.tokenprovider.token.PrivilegeJwtToken;
import org.mint.smallcloud.user.domain.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Component
public class JwtTokenFactory {
    private final JwtTokenProperties jwtTokenProperties;

    public JwtTokenFactory(JwtTokenProperties jwtTokenProperties) {
        this.jwtTokenProperties = jwtTokenProperties;
    }

    public JwtToken createWithRole(String username, LocalDateTime now, Role role) {
        return createNotPreparedToken(role).prepare(username, now, createPropertiesDto(), Collections.emptyMap());
    }

    public JwtToken createWithRole(String username, LocalDateTime now, Role role, Map<String, Object> payload) {
        return createNotPreparedToken(role).prepare(username, now, createPropertiesDto(), payload);
    }

    public JwtToken createCommon(String username, LocalDateTime now, Map<String, Object> payload) {
        return createWithRole(username, now, Role.COMMON, payload);
    }

    public JwtToken createAdmin(String username, LocalDateTime now, Map<String, Object> payload) {
        return createWithRole(username, now, Role.ADMIN, payload);
    }

    public JwtToken createPrivilege(String username, LocalDateTime now, Map<String, Object> payload) {
        return createWithRole(username, now, Role.PRIVILEGE, payload);
    }

    private JwtTokenPropertiesDto createPropertiesDto() {
        return JwtTokenPropertiesDto.builder()
            .grantType(jwtTokenProperties.getGrantType())
            .key(jwtTokenProperties.getTokenKey())
            .roleField(jwtTokenProperties.getRoleField())
            .signatureAlgorithm(jwtTokenProperties.getSignaturealgorithm())
            .tokenTypeHeader(jwtTokenProperties.getTypeHeaderField())
            .build();
    }

    private JwtToken createNotPreparedToken(Role role) {
        switch (role) {
            case ADMIN:
                return new AdminJwtToken();
            case COMMON:
                return new CommonJwtToken();
            case PRIVILEGE:
                return new PrivilegeJwtToken();
            default:
                throw new ServiceException(ExceptionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
