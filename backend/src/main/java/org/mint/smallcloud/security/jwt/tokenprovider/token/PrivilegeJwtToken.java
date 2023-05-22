package org.mint.smallcloud.security.jwt.tokenprovider.token;

import lombok.NoArgsConstructor;
import org.mint.smallcloud.user.domain.Roles;

@NoArgsConstructor
public class PrivilegeJwtToken extends JwtToken {
    private static final String ROLE = Roles.PRIVILEGE;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 60 * 60 * 3; // 3시간

    @Override
    protected String getRole() {
        return ROLE;
    }

    @Override
    protected long getAccessTokenExpireSec() {
        return ACCESS_TOKEN_EXPIRE_TIME;
    }

    @Override
    protected long getRefreshTokenExpireSec() {
        return REFRESH_TOKEN_EXPIRE_TIME;
    }
}
