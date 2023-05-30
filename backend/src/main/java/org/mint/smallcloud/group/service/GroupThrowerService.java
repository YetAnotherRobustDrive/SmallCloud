package org.mint.smallcloud.group.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.group.repository.GroupRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupThrowerService {
    private final GroupRepository groupRepository;

    public Group getGroupByName(String groupName) {
        return groupRepository.findByName(groupName)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_GROUP_NAME));
    }

    public void checkDuplicateGroupName(String groupName) {
        if (groupRepository.existsByName(groupName))
            throw new ServiceException(ExceptionStatus.GROUP_NAME_ALREADY_EXISTS);
    }
}
