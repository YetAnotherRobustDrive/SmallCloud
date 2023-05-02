package org.mint.smallcloud.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.user.Roles;
import org.mint.smallcloud.user.UserService;
import org.mint.smallcloud.user.dto.UserDetailsDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        log.info("called JwtUserDetailService");
        UserDetailsDto userDetails = userService.getUserDetails(userName);
        return User.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .roles(Roles.COMMON.getRole())
                .disabled(userDetails.isDisabled())
                .build();
    }
}
