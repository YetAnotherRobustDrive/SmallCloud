package org.mint.smallcloud.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserExceptionService {
    private final UserRepository userRepository;

    public User getUserByLoginId(String loginId) {
        return userRepository
                .findByLoginId(loginId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_USER));
    }

    public void checkExistsLoginId(String loginId) {
        if (userRepository.existsByLoginId(loginId))
            throw new ServiceException(ExceptionStatus.USER_ALREADY_EXISTS);
    }
}
