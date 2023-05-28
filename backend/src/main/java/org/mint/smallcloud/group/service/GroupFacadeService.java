package org.mint.smallcloud.group.service;

import org.mint.smallcloud.group.dto.GroupCreateDto;
import org.mint.smallcloud.group.dto.GroupTreeDto;
import org.mint.smallcloud.group.dto.GroupUpdateDto;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class GroupFacadeService {

    public void create(@Valid GroupCreateDto groupCreateDto) {
    }

    public void delete(String groupId) {
    }

    public void addUser(String groupId, String username) {
    }

    public void update(String groupId, @Valid GroupUpdateDto groupUpdateDto) {
    }

    public void deleteUser(String groupId, String userId) {
    }

    public GroupTreeDto readGroupTree(String groupId) {
    }

    public List<UserProfileResponseDto> getUserList(String groupId) {
        return null;
    }
}
