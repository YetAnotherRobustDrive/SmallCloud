package org.mint.smallcloud.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserThrowerService {
    private final UserRepository userRepository;

    public Member getUserByLoginId(String loginId) {
        return userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_USER));
    }

    public void checkExistsLoginId(String loginId) {
        if (userRepository.existsByLoginId(loginId))
            throw new ServiceException(ExceptionStatus.USER_ALREADY_EXISTS);
    }
}
