package org.mint.smallcloud.user.service;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.exception.ServiceException;
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
        Member member = memberThrowerService.getMemberByUsername(username);
        try {
            authThrowerService.checkLoginMember(username);
        } catch (ServiceException e) {
            authThrowerService.checkLoginAdmin();
        }
        return memberService.getProfile(username);
    }
}
