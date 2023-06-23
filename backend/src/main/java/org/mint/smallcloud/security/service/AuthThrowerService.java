package org.mint.smallcloud.security.service;

import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.security.mapper.UserDetailsResolver;
import org.mint.smallcloud.user.domain.Role;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthThrowerService {
    private final UserDetailsProvider userDetailsProvider;
    private final UserDetailsResolver userDetailsResolver;

    public AuthThrowerService(UserDetailsProvider userDetailsProvider, UserDetailsResolver userDetailsResolver) {
        this.userDetailsProvider = userDetailsProvider;
        this.userDetailsResolver = userDetailsResolver;
    }

    public UserDetails getLoginUserDetails() {
        return userDetailsProvider.getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
    }

    public void checkLoginAdmin() {
        if (!userDetailsResolver.getRole(getLoginUserDetails())
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION))
            .equals(Role.ADMIN))
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
    }

    public void checkLoginMember(String username) {
        String loginUsername = getLoginUserDetails().getUsername();
        if (!loginUsername.equals(username))
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
    }
}
