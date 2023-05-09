package org.mint.smallcloud.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.dto.RegisterDto;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.domain.Roles;
import org.mint.smallcloud.user.dto.UserDetailsDto;
import org.mint.smallcloud.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserThrowerService userExceptionService;
    private final UserRepository userRepository;

    public UserDetailsDto getUserDetails(String loginId) {
        Member member = userExceptionService.getUserByLoginId(loginId);
        return UserDetailsDto.builder()
            .username(member.getLoginId())
            .password(member.getPassword())
            .roles(Roles.COMMON)
            .disabled(member.isLocked())
            .build();
    }

    public void registerUser(RegisterDto registerDto) {
        userExceptionService.checkExistsLoginId(registerDto.getId());
        Member member = Member.of(
            registerDto.getId(),
            registerDto.getPassword(),
            registerDto.getName());
        userRepository.save(member);
    }

    public void deregisterUser(String userId) {
        Member member = userExceptionService.getUserByLoginId(userId);
        userRepository.delete(member);
    }

    public void checkPassword(LoginDto loginDto) {
        Member member = userExceptionService.getUserByLoginId(loginDto.getId());
        if (!member.getPassword().equals(loginDto.getPassword()))
            throw new ServiceException(ExceptionStatus.WRONG_PASSWORD);
    }

    public List<String> searchUser(String query) {
        return new ArrayList<>();
    }

    public String getUserInfo(Long userId) {
        return "";
    }

    public void deactivateUser() {
    }

    public void resetUserPw() {
    }
}
