package org.mint.smallcloud.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * spring security context에 user정보를 저장하고, 가져오는 class
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserDetailsProvider {
    /**
     * 유저의 id를 가져온다.
     * @return userId를 담은 {@link Optional}
     */
    public Optional<String> getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return Optional.of(((UserDetails) principal).getUsername());
        } else {
            return Optional.empty();
        }
    }


    /**
     * {@link UserDetails}를 spring context에 저장합니다.
     *
     * @param authentication 저장할 인증정보
     * @throws ServiceException {@link Authentication}이 {@link UserDetails}가 아니라면 internal server error로 throw
     */
    public void setAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.error("unexpected authentication");
            throw new ServiceException(ExceptionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
