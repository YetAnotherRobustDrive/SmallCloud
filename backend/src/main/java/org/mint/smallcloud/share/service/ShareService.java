package org.mint.smallcloud.share.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.dto.DirectoryDto;
import org.mint.smallcloud.file.dto.FileDto;
import org.mint.smallcloud.file.mapper.FileMapper;
import org.mint.smallcloud.file.mapper.FolderMapper;
import org.mint.smallcloud.file.repository.DataNodeRepository;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.group.service.GroupThrowerService;
import org.mint.smallcloud.notification.event.NoticeEventAfterCommit;
import org.mint.smallcloud.share.domain.GroupShare;
import org.mint.smallcloud.share.domain.MemberShare;
import org.mint.smallcloud.share.dto.ShareRequestDto;
import org.mint.smallcloud.share.repository.GroupShareRepository;
import org.mint.smallcloud.share.repository.MemberShareRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

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
    private final FileMapper fileMapper;
    private final FolderMapper folderMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    public void create(String loginUsername, ShareRequestDto dto) {
        Member accessor = memberThrowerService.getMemberByUsername(loginUsername);
        DataNode file = dataNodeRepository.findById(dto.getFileId())
            .orElseThrow(() -> new ServiceException(ExceptionStatus.FILE_NOT_FOUND));
        if (!file.canAccessUser(accessor))
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        List<Member> members = new ArrayList<>();
        switch (dto.getType()) {
            case GROUP:
                Group group = groupThrowerService.getGroupByName(dto.getTargetName());
                createGroupShare(group, file);
                members.addAll(group.getMembers());
                break;
            case MEMBER:
                Member member = memberThrowerService.getMemberByUsername(dto.getTargetName());
                createMemberShare(member, file);
                members.add(member);
                break;
            default:
                throw new ServiceException(ExceptionStatus.INVALID_PARAMETER);
        }
        members.forEach(member ->
            applicationEventPublisher.publishEvent(
                NoticeEventAfterCommit.builder()
                    .content(String.format("\"%s\"을(를) 공유 받았습니다.", file.getName()))
                    .owner(member)
                    .build()));
        }

    public void delete(String loginUsername, ShareRequestDto dto) {
        Member accessor = memberThrowerService.getMemberByUsername(loginUsername);
        DataNode file = dataNodeRepository.findById(dto.getFileId())
            .orElseThrow(() -> new ServiceException(ExceptionStatus.FILE_NOT_FOUND));
        if (!file.canAccessUser(accessor))
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        List<Member> members = new ArrayList<>();
        switch (dto.getType()) {
            case GROUP:
                members.addAll(groupThrowerService.getGroupByName(dto.getTargetName()).getMembers());
                groupShareRepository.deleteByFileIdAndTarget_Name(file.getId(), dto.getTargetName());
                break;
            case MEMBER:
                members.add(memberThrowerService.getMemberByUsername(dto.getTargetName()));
                memberShareRepository.deleteByFileIdAndTarget_Username(file.getId(), dto.getTargetName());
                break;
            default:
                throw new ServiceException(ExceptionStatus.INVALID_PARAMETER);
        }
        members.forEach(member ->
            applicationEventPublisher.publishEvent(
                NoticeEventAfterCommit.builder()
                    .content(String.format("\"%s\"을(를) 공유 해제했습니다", file.getName()))
                    .owner(member)
                    .build()));
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

    public List<FileDto> getFileList(String username) {
        Member member = memberThrowerService.getMemberByUsername(username);
        Group group = member.getGroup();
        List<MemberShare> shares = member.getShares();
        List<FileDto> files = shares.stream()
            .filter(share -> share.getFile().isFile())
            .map(share -> fileMapper.toFileDto((File) share.getFile())).collect(Collectors.toList());
        if (group != null) {
            List<GroupShare> groupShares = group.getShares();
            files.addAll(groupShares.stream()
                .filter(share -> share.getFile().isFile())
                .map(share -> fileMapper.toFileDto((File) share.getFile()))
                .collect(Collectors.toList()));
        }
        return files;
    }

    public List<DirectoryDto> getDirectoryList(String username) {
        Member member = memberThrowerService.getMemberByUsername(username);
        Group group = member.getGroup();
        List<MemberShare> shares = member.getShares();
        List<DirectoryDto> files = shares.stream()
            .filter(share -> share.getFile().isFolder())
            .map(share -> folderMapper.toDirectoryDto((Folder) share.getFile())).collect(Collectors.toList());
        if (group != null) {
            List<GroupShare> groupShares = group.getShares();
            files.addAll(groupShares.stream()
                .filter(share -> share.getFile().isFolder())
                .map(share -> folderMapper.toDirectoryDto((Folder) share.getFile()))
                .collect(Collectors.toList()));
        }
        return files;
    }
}
