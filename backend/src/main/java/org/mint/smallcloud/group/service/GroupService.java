package org.mint.smallcloud.group.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.group.repository.GroupRepository;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupThrowerService groupThrowerService;

    public void createRootGroup(String groupName) {
        groupThrowerService.checkDuplicateGroupName(groupName);
        groupRepository.save(Group.of(groupName));
    }

    public void createSubGroup(String groupName, String parentName) {
        groupThrowerService.checkDuplicateGroupName(groupName);
        Group parent = groupThrowerService.getGroupByName(parentName);
        groupRepository.save(Group.of(groupName, parent));
    }

    public void delete(String groupName) {
        Group group = groupThrowerService.getGroupByName(groupName);
        group.getMembers().forEach(Member::unsetGroup);
        groupRepository.delete(group);
    }

    public List<String> search(String q) {
        if (q.isBlank())
            return List.of();
        return groupRepository
            .findByNameLike("%" + q + "%")
            .stream().map(Group::getName).collect(Collectors.toList());
    }
}
