package org.mint.smallcloud.security.mapper;

import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.user.domain.Role;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsResolver {
    public Optional<Role> getRole(UserDetails userDetails) {
        Optional<Role> ret = Optional.empty();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.getAuthority().startsWith(Roles.PREFIX)) {
                ret = Role.of(authority.getAuthority()
                    .substring(Roles.PREFIX.length()));
            }
        }
        return ret;
    }

    public UserDetailsDto toUserDetailsDto(UserDetails userDetails) {
        Optional<Role> role = getRole(userDetails);
        return UserDetailsDto.builder()
            .disabled(!userDetails.isEnabled())
            .username(userDetails.getUsername())
            .password(userDetails.getPassword())
            .roles(role.orElseThrow(IllegalArgumentException::new))
            .build();
    }

    public UserDetails toUserDetails(UserDetailsDto userDetailsDto) {
        return User.builder()
            .disabled(userDetailsDto.isDisabled())
            .username(userDetailsDto.getUsername())
            .password(userDetailsDto.getPassword())
            .roles(userDetailsDto.getRoles().getValue())
            .build();
    }
}
