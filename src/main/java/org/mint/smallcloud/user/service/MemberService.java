package org.mint.smallcloud.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.RegisterDto;
import org.mint.smallcloud.user.dto.UserProfileRequestDto;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
import org.mint.smallcloud.user.mapper.UserMapper;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberThrowerService memberThrowerService;
    private final MemberRepository memberRepository;
    private final UserMapper userMapper;

    public UserDetailsDto getUserDetails(String username) {
        Member member = memberThrowerService.getMemberByUsername(username);
        return UserDetailsDto.builder()
            .username(member.getUsername())
            .password(member.getPassword())
            .roles(member.getRole())
            .disabled(member.isLocked())
            .build();
    }

    public void registerCommon(RegisterDto registerDto) {
        memberThrowerService.checkExistsByUsername(registerDto.getId());
        Member member = Member.of(
            registerDto.getId(),
            registerDto.getPassword(),
            registerDto.getName());
        memberRepository.save(member);
    }

    public void deregisterCommon(String username) {
        Member member = memberThrowerService.getCommonByUsername(username);
        memberRepository.delete(member);
    }

    public void checkPassword(LoginDto loginDto) {
        memberThrowerService.checkPassword(loginDto.getId(), loginDto.getPassword());
    }

    public UserProfileResponseDto getProfile(String username) {
        Member member = memberThrowerService.getMemberByUsername(username);
        return userMapper.toUserProfileResponseDto(member);
    }

    public void updateProfile(Member member, UserProfileRequestDto userProfileDto) {
        if (!userProfileDto.getUsername().equals(member.getUsername())) {
            if (memberRepository.existsByUsername(userProfileDto.getUsername()))
                throw new ServiceException(ExceptionStatus.USERNAME_ALREADY_EXISTS);
            member.setUsername(userProfileDto.getUsername());
        }
        if (!userProfileDto.getNickname().equals(member.getUsername())) {
            member.setNickname(userProfileDto.getNickname());
        }
        if (!userProfileDto.getGroupName().equals(member.getGroup().getName())) {
            /* TODO: group이 존재하는 group인지 확인하는 로직 추가 */
            /* TODO: group 변경 추가 */
            member.setGroup(null);
        }
        if (!userProfileDto.getProfileImageLocation().equals(member.getProfileImageLocation())) {
            /* TODO: location이 존재하는 location인지 확인하는 로직 추가 */
            member.setProfileImageLocation(member.getProfileImageLocation());
        }
    }
}
