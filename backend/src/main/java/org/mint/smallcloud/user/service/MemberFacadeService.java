package org.mint.smallcloud.user.service;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.file.repository.FolderRepository;
import org.mint.smallcloud.security.service.AuthThrowerService;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberFacadeService {
    private final MemberService memberService;
    private final MemberThrowerService memberThrowerService;
    private final AuthThrowerService authThrowerService;
    private final FolderRepository folderRepository;
    private final ProfilePhotoService photoService;

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

    public List<String> search(String q) {
        return memberService.search(q);
    }

    public Long getRootDir(String username) {
        return folderRepository.findByParentFolderIsNullAndAuthor(memberThrowerService.getMemberByUsername(username))
            .orElseThrow(() -> new ServiceException(ExceptionStatus.INTERNAL_SERVER_ERROR))
            .getId();
    }

    public void updatePhoto(String username, MultipartFile imageFile)
        throws Exception {
        memberService.updatePhoto(memberThrowerService.getMemberByUsername(username),
                                  imageFile);
    }

    public PhotoDownloadResponseDto downloadPhoto(String username) {
        Member member = memberThrowerService.getMemberByUsername(username);
        FileLocation location = member.getProfileImageLocation();
        return photoService.downloadPhoto(location);
    }

    public void updatePassword(String username, PasswordUpdateRequestDto dto) {
        Member member = memberThrowerService.getMemberByUsername(username);
        if (member.verifyPassword(dto.getPassword()))
            memberService.updatePassword(member, dto.getNewPassword());
        else
            throw new ServiceException(ExceptionStatus.WRONG_PASSWORD);
    }
}
