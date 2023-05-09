package org.mint.smallcloud.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.mapper.UserDetailsResolver;
import org.mint.smallcloud.user.domain.Role;
import org.mint.smallcloud.user.domain.Roles;
import org.mint.smallcloud.user.service.MemberService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final MemberService userService;
    private final UserDetailsResolver userDetailsResolver;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        UserDetailsDto userDetails =
            userService.getUserDetails(userName);
        return userDetailsResolver.toUserDetails(userDetails);
    }

    public UserDetails loadUserByUsernameAndRole(String userName, Role role) {
        return getElevatedUser(loadUserByUsername(userName), role);
    }

    public UserDetails getElevatedUser(UserDetails user, Role targetRole) {
        if (Role.PRIVILEGE.equals(targetRole)
            && user.isEnabled()
            && !Role.ADMIN.equals(userDetailsResolver.getRole(user)
            .orElse(null)))
            return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(Roles.PRIVILEGE)
                .disabled(true)
                .build();
        return user;
    }
}
