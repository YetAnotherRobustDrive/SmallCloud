package org.mint.smallcloud.group.service;

import org.mint.smallcloud.group.dto.GroupRequestDto;
import org.mint.smallcloud.group.dto.GroupTreeDto;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupFacadeService {

    public void create(GroupRequestDto groupRequestDto) {
        
    }

    public void delete(String groupId) {
    }

    public void addUser(String groupId, String username) {
    }

    public void update(String groupId, GroupRequestDto groupRequestDto) {
    }

    public void deleteUser(String groupId, String userId) {
    }

    public GroupTreeDto readGroupTree(String groupId) {
        return null;
    }

    public List<UserProfileResponseDto> getUserList(String groupId) {
        return null;
    }
}
