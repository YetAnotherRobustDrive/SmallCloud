package org.mint.smallcloud.security.jwt;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.user.UserService;
import org.mint.smallcloud.user.dto.UserDetailsDto;
import org.mint.smallcloud.user.exception.NotFoundUserException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private static final String USER = "USER";

    @Override
    public UserDetails loadUserByUsername(String userName) {
        log.info("called JwtUserDetailService");
        UserDetailsDto userDetails = userService.getUserDetails(userName);

        return User.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .roles(USER)
                .disabled(userDetails.isDisabled())
                .build();
    }
}
