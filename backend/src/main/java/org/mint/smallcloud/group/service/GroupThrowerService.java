package org.mint.smallcloud.group.service;

import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.group.repository.GroupRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class GroupThrowerService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GroupThrowerService.class);
    private final GroupRepository groupRepository;

    public GroupThrowerService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group getGroupByName(String groupName) {
        return groupRepository.findByName(groupName)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_GROUP_NAME));
    }

    public void checkDuplicateGroupName(String groupName) {
        if (groupRepository.existsByName(groupName))
            throw new ServiceException(ExceptionStatus.GROUP_NAME_ALREADY_EXISTS);
    }
}
