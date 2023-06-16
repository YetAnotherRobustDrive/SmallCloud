package org.mint.smallcloud.admin.service;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.admin.domain.AdminConfig;
import org.mint.smallcloud.admin.dto.AdminConfigDto;
import org.mint.smallcloud.admin.dto.ChangePasswordDto;
import org.mint.smallcloud.admin.repository.AdminConfigRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final MemberThrowerService memberThrowerService;
    private final AdminConfigRepository adminConfigRepository;

    public void lockUser(String username, boolean lock) {
        Member member = memberThrowerService.getCommonByUsername(username);
        if (lock) member.lock();
        else member.unlock();
    }

    public void resetPassword(String username, ChangePasswordDto dto) {
        Member member = memberThrowerService.getCommonByUsername(username);
        member.changePassword(dto.getPassword());
    }

    public void requestConfig(AdminConfigDto adminConfigDto) {
        AdminConfig adminConfig = AdminConfig.of(
            adminConfigDto.getCode(),
            adminConfigDto.getValue()
        );
        adminConfigRepository.save(adminConfig);
    }

    public AdminConfig responseConfig(String code) {
        return adminConfigRepository
            .findByCodeOrderByLocalDateTimeDesc(code);
    }
}
