package org.mint.smallcloud.group.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.group.dto.GroupRequestDto;
import org.mint.smallcloud.group.dto.GroupTreeDto;
import org.mint.smallcloud.group.repository.GroupRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
import org.mint.smallcloud.user.mapper.UserMapper;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GroupFacadeService {

    private final GroupRepository groupRepository;
    private final GroupThrowerService groupThrowerService;
    private final MemberThrowerService memberThrowerService;
    private final UserMapper userMapper;

    public void create(GroupRequestDto groupRequestDto) {
        groupThrowerService.checkDuplicateGroupName(groupRequestDto.getGroupName());
        if (groupRequestDto.getParentName() == null)
            groupRepository.save(Group.of(groupRequestDto.getGroupName()));
        else {
            Group parent = groupThrowerService.getGroupByName(groupRequestDto.getParentName());
            groupRepository.save(Group.of(groupRequestDto.getGroupName(), parent));
        }
    }

    public void delete(String groupName) {
        Group group = groupThrowerService.getGroupByName(groupName);
        group.getMembers().forEach(Member::unsetGroup);
        groupRepository.delete(group);
    }

    public void addUser(String groupId, String username) {
        Group group = groupThrowerService.getGroupByName(groupId);
        Member member = memberThrowerService.getMemberByUsername(username);
        member.setGroup(group);
    }

    public void update(String groupName, GroupRequestDto groupRequestDto) {
        Group group = groupThrowerService.getGroupByName(groupName);
        if (groupRequestDto.getParentName() != null) {
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
        if (member.getGroup().equals(group))
            member.unsetGroup();
    }

    public GroupTreeDto readGroupTree(String groupName) {
        Group group = groupThrowerService.getGroupByName(groupName);
        return GroupTreeDto.builder()
                .name(group.getName())
                .subGroups(getSubGroups(group))
            .build();
    }

    private List<GroupTreeDto> getSubGroups(Group group) {
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
}
