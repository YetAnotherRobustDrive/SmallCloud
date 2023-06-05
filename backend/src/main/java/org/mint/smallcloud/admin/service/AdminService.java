package org.mint.smallcloud.admin.service;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.admin.dto.ChangePasswordDto;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberThrowerService memberThrowerService;

    public void lockUser(String username, boolean lock) {
        Member member = memberThrowerService.getCommonByUsername(username);
        if (lock) member.lock();
        else member.unlock();
    }

    public void resetPassword(String username, ChangePasswordDto dto) {
        Member member = memberThrowerService.getCommonByUsername(username);
        member.changePassword(dto.getPassword());
    }
}
