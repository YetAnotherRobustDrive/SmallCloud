package org.mint.smallcloud.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.bucket.exception.StorageSettingException;
import org.mint.smallcloud.bucket.service.StorageService;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.service.DirectoryService;
import org.mint.smallcloud.group.service.GroupThrowerService;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.RegisterDto;
import org.mint.smallcloud.user.dto.UserProfileRequestDto;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
import org.mint.smallcloud.user.mapper.UserMapper;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberThrowerService memberThrowerService;
    private final MemberRepository memberRepository;
    private final GroupThrowerService groupThrowerService;
    private final UserMapper userMapper;
    private final StorageService storageService;
    private final DirectoryService directoryService;

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
        directoryService.createRootDirectory(member);
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
        if (userProfileDto.getUsername() != null && !userProfileDto.getUsername().equals(member.getUsername())) {
            if (memberRepository.existsByUsername(userProfileDto.getUsername()))
                throw new ServiceException(ExceptionStatus.USERNAME_ALREADY_EXISTS);
            member.setUsername(userProfileDto.getUsername());
        }
        if (userProfileDto.getNickname() != null && !userProfileDto.getNickname().equals(member.getUsername())) {
            member.setNickname(userProfileDto.getNickname());
        }
        if (userProfileDto.getGroupName() != null && (!member.hasGroup() || !userProfileDto.getGroupName().equals(member.getGroupName()))) {
            member.setGroup(groupThrowerService.getGroupByName(userProfileDto.getGroupName()));
        }
        if (userProfileDto.getProfileImageLocation() != null && !userProfileDto.getProfileImageLocation().equals(member.getProfileImageLocation())) {
            try {
                if (!storageService.isFileExist(userProfileDto.getProfileImageLocation().getLocation()))
                    throw new ServiceException(ExceptionStatus.FILE_NOT_FOUND);
            } catch (StorageSettingException e) {
                e.printStackTrace();
                throw new ServiceException(ExceptionStatus.INTERNAL_SERVER_ERROR);
            }
            member.setProfileImageLocation(member.getProfileImageLocation());
        }
    }
}
