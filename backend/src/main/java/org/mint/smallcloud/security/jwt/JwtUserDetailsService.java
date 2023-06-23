package org.mint.smallcloud.security.jwt;

import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.mapper.UserDetailsResolver;
import org.mint.smallcloud.user.domain.Role;
import org.mint.smallcloud.user.service.MemberService;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JwtUserDetailsService.class);
    private final MemberService userService;
    private final UserDetailsResolver userDetailsResolver;

    public JwtUserDetailsService(MemberService userService, UserDetailsResolver userDetailsResolver) {
        this.userService = userService;
        this.userDetailsResolver = userDetailsResolver;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        UserDetailsDto userDetails =
            userService.getUserDetails(userName);
        return userDetailsResolver.toUserDetails(userDetails);
    }

    public UserDetails loadUserByUsernameAndRole(String userName, Role role) {
        return userDetailsResolver.getElevatedUser(loadUserByUsername(userName), role);
    }
}
