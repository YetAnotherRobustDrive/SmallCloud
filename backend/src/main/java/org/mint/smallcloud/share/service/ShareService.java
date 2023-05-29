package org.mint.smallcloud.share.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.file.repository.DataNodeRepository;
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
    private final GroupThrowerService groupThrowerService;
    private final DataNodeRepository dataNodeRepository;
    public void create(String loginUsername, ShareRequestDto dto) {
        Member accessor = memberThrowerService.getMemberByUsername(loginUsername);
        DataNode file = dataNodeRepository.findById(dto.getFileId())
            .orElseThrow(() -> new ServiceException(ExceptionStatus.FILE_NOT_FOUND));
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
        DataNode file = dataNodeRepository.findById(dto.getFileId())
            .orElseThrow(() -> new ServiceException(ExceptionStatus.FILE_NOT_FOUND));
        if (!file.canAccessUser(accessor))
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        switch (dto.getType()) {
            case GROUP:
                groupShareRepository.deleteByFileIdAndTarget_Name(file.getId(), dto.getTargetName());
                break;
            case MEMBER:
                memberShareRepository.deleteByFileIdAndTarget_Username(file.getId(), dto.getTargetName());
                break;
            default:
                throw new ServiceException(ExceptionStatus.INVALID_PARAMETER);
        }
    }

    private void createGroupShare(Group target, DataNode file) {
        if (groupShareRepository.existsByFileIdAndTarget_Name(file.getId(), target.getName()))
            return;
        groupShareRepository.save(GroupShare.of(target, file));
    }
    private void createMemberShare(Member target, DataNode file) {
        if (memberShareRepository.existsByFileIdAndTarget_Username(file.getId(), target.getUsername()))
            return;
        memberShareRepository.save(MemberShare.of(target, file));
    }
}
