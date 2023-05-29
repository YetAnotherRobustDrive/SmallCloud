package org.mint.smallcloud.share.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.service.FileThrowerService;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.group.service.GroupThrowerService;
import org.mint.smallcloud.share.domain.GroupShare;
import org.mint.smallcloud.share.domain.MemberShare;
import org.mint.smallcloud.share.dto.ShareRequestDto;
import org.mint.smallcloud.share.repository.GroupShareRepository;
import org.mint.smallcloud.share.repository.MemberShareRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShareService {

    private final GroupShareRepository groupShareRepository;
    private final MemberShareRepository memberShareRepository;
    private final MemberThrowerService memberThrowerService;
    private final FileThrowerService fileThrowerService;
    private final GroupThrowerService groupThrowerService;
    public void create(String loginUsername, ShareRequestDto dto) {
        Member accessor = memberThrowerService.getMemberByUsername(loginUsername);
        File file = fileThrowerService.getFileById(dto.getFileId());
        if (!file.canAccessUser(accessor))
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        switch (dto.getType()) {
            case GROUP:
                createGroupShare(groupThrowerService.getGroupByName(dto.getTargetName()), file);
                break;
            case MEMBER:
                createMemberShare(memberThrowerService.getMemberByUsername(dto.getTargetName()), file);
                break;
            default:
                throw new ServiceException(ExceptionStatus.INVALID_PARAMETER);
        }
    }

    public void delete(String loginUsername, ShareRequestDto dto) {
        Member accessor = memberThrowerService.getMemberByUsername(loginUsername);
        File file = fileThrowerService.getFileById(dto.getFileId());
        if (!file.canAccessUser(accessor))
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        switch (dto.getType()) {
            case GROUP:
                groupShareRepository.deleteByFileIdAndTargetName(file.getId(), dto.getTargetName());
                break;
            case MEMBER:
                memberShareRepository.deleteByFileIdAndTargetName(file.getId(), dto.getTargetName());
                break;
            default:
                throw new ServiceException(ExceptionStatus.INVALID_PARAMETER);
        }
    }

    private void createGroupShare(Group target, File file) {
        if (groupShareRepository.existsByFileIdAndTargetName(file.getId(), target.getName()))
            return;
        groupShareRepository.save(GroupShare.of(target, file));
    }
    private void createMemberShare(Member target, File file) {
        if (memberShareRepository.existsByFileIdAndTargetName(file.getId(), target.getUsername()))
            return;
        memberShareRepository.save(MemberShare.of(target, file));
    }
}
