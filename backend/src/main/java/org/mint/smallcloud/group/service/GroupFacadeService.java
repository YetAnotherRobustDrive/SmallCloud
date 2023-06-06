package org.mint.smallcloud.group.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.group.dto.GroupRequestDto;
import org.mint.smallcloud.group.dto.GroupTreeDto;
import org.mint.smallcloud.notification.event.NoticeEventAfterCommit;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
import org.mint.smallcloud.user.mapper.UserMapper;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GroupFacadeService {

    private final GroupThrowerService groupThrowerService;
    private final MemberThrowerService memberThrowerService;
    private final GroupService groupService;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void create(GroupRequestDto groupRequestDto) {
        if (groupRequestDto.getParentName() == null)
            groupService.createRootGroup(groupRequestDto.getGroupName());
        else
            groupService.createSubGroup(groupRequestDto.getGroupName(), groupRequestDto.getParentName());

    }

    public void delete(String groupName) {
        groupService.delete(groupName);
    }

    public void addUser(String groupId, String username) {
        Group group = groupThrowerService.getGroupByName(groupId);
        group.getMembers().forEach(member -> {
            applicationEventPublisher.publishEvent(
                NoticeEventAfterCommit.builder()
                    .owner(member)
                    .content(String.format("\"%s\" 그룹에 \"%s\"님이 추가되었습니다.", group.getName(), username))
            );
        });
        Member member = memberThrowerService.getMemberByUsername(username);
        member.setGroup(group);
    }

    public void update(String groupName, GroupRequestDto groupRequestDto) {
        Group group = groupThrowerService.getGroupByName(groupName);
        if (groupRequestDto.getGroupName() != null) {
            groupThrowerService.checkDuplicateGroupName(groupRequestDto.getGroupName());
            group.setName(groupRequestDto.getGroupName());
        }
        if (groupRequestDto.getParentName() != null) {
            Group parent = groupThrowerService.getGroupByName(groupRequestDto.getParentName());
            group.setParentGroup(parent);
        }
    }

    public void deleteUser(String groupName, String username) {
        Group group = groupThrowerService.getGroupByName(groupName);
        Member member = memberThrowerService.getMemberByUsername(username);
        if (member.getGroup().equals(group)) {
            applicationEventPublisher.publishEvent(
                NoticeEventAfterCommit.builder()
                    .owner(member)
                    .content(String.format("\"%s\" 그룹에서 \"%s\"님이 탈퇴되었습니다.", group.getName(), username))
            );
            member.unsetGroup();
        }
    }

    public GroupTreeDto readGroupTree(String groupName) {
        Group group = groupThrowerService.getGroupByName(groupName);
        return GroupTreeDto.builder()
                .name(group.getName())
                .subGroups(getSubGroups(group))
            .build();
    }

    private List<GroupTreeDto> getSubGroups(Group group) {
        System.out.println(group.getSubGroups().size());
        return group.getSubGroups().stream()
                .map(subGroup -> GroupTreeDto.builder()
                        .name(subGroup.getName())
                        .subGroups(getSubGroups(subGroup))
                    .build())
                .collect(Collectors.toList());
    }

    public List<UserProfileResponseDto> getUserList(String groupName) {
        Group group = groupThrowerService.getGroupByName(groupName);
        return group.getMembers().stream().map(userMapper::toUserProfileResponseDto).collect(Collectors.toList());
    }

    public List<String> search(String q) {
        return groupService.search(q);
    }
}
