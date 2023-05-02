package org.mint.smallcloud.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.dto.RegisterDto;
import org.mint.smallcloud.user.dto.UserDetailsDto;
import org.mint.smallcloud.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserExceptionService userExceptionService;
    private final UserRepository userRepository;
    public UserDetailsDto getUserDetails(String loginId) {
        User user = userExceptionService.getUserByLoginId(loginId);
        return UserDetailsDto.builder()
                .username(user.getLoginId())
                .password(user.getPassword())
                .disabled(user.isLocked())
                .build();
    }

    public void registerUser(RegisterDto registerDto) {
        userExceptionService.checkExistsLoginId(registerDto.getId());
        User user = User.of(
                registerDto.getId(),
                registerDto.getPassword(),
                registerDto.getName());
        userRepository.save(user);
    }

    public void checkPassword(LoginDto loginDto) {
        User user = userExceptionService.getUserByLoginId(loginDto.getId());
        if (!user.getPassword().equals(loginDto.getPassword()))
            throw new ServiceException(ExceptionStatus.WRONG_PASSWORD);
    }

    public List<String> searchUser(String query) { return new ArrayList<>(); }
    public String getUserInfo(Long userId) {return "";}
    public void deactivateUser() {}
    public void resetUserPw() {}
}
