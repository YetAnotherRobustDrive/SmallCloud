package org.mint.smallcloud.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.user.dto.UserDetailsDto;
import org.mint.smallcloud.user.exception.NotFoundUserException;
import org.mint.smallcloud.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public UserDetailsDto getUserDetails(String loginId) {
        User user = userRepository
                .findByLoginId(loginId)
                .orElseThrow(()->new ServiceException(ExceptionStatus.NOT_FOUND_USER));
        return UserDetailsDto.builder()
                .username(user.getLoginId())
                .password(user.getPassword())
                .disabled(user.isLocked())
                .build();
    }
    public List<String> searchUser(String query) { return new ArrayList<>(); }
    public String getUserInfo(Long userId) {return "";}
    public void deactivateUser() {}
    public void resetUserPw() {}
}
