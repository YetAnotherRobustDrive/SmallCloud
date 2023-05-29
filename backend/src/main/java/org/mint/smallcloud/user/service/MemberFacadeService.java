package org.mint.smallcloud.user.service;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.repository.FileRepository;
import org.mint.smallcloud.file.repository.FolderRepository;
import org.mint.smallcloud.security.service.AuthThrowerService;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.RegisterDto;
import org.mint.smallcloud.user.dto.UserProfileRequestDto;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberFacadeService {
    private final MemberService memberService;
    private final MemberThrowerService memberThrowerService;
    private final AuthThrowerService authThrowerService;
    private final FolderRepository folderRepository;

    public void delete(String username) {
        memberService.deregisterCommon(username);
    }

    public void register(RegisterDto registerDto) {
        memberService.registerCommon(registerDto);
    }

    public void update(String username, UserProfileRequestDto userProfileDto) {
        Member member = memberThrowerService.getMemberByUsername(username);
        try {
            authThrowerService.checkLoginMember(username);
        } catch (ServiceException e) {
            authThrowerService.checkLoginAdmin();
        }
        memberService.updateProfile(member, userProfileDto);
    }

    public UserProfileResponseDto profile(String username) {
        UserProfileResponseDto rst = memberService.getProfile(username);
        try {
            authThrowerService.checkLoginMember(username);
        } catch (ServiceException e) {
            authThrowerService.checkLoginAdmin();
        }
        return rst;
    }

    public Long getRootDir(String username) {
        return folderRepository.findByParentFolderIsNullAndAuthor(memberThrowerService.getMemberByUsername(username))
            .orElseThrow(() -> new ServiceException(ExceptionStatus.INTERNAL_SERVER_ERROR))
            .getId();
    }
}
