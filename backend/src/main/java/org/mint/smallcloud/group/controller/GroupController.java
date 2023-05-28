package org.mint.smallcloud.group.controller;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.group.dto.GroupRequestDto;
import org.mint.smallcloud.group.dto.GroupTreeDto;
import org.mint.smallcloud.group.service.GroupFacadeService;
import org.mint.smallcloud.user.domain.Roles;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupFacadeService groupFacadeService;
    // create
    @Secured(Roles.S_ADMIN)
    @PostMapping("/create")
    public void update(@Valid @RequestBody List<GroupRequestDto> groupRequestDtos) {
         groupFacadeService.update(groupRequestDtos);
    }

    @RequestMapping(value = "/{groupName}/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public void delete(@PathVariable("groupName") String groupName) {
         groupFacadeService.delete(groupName);
    }

    // addUser
    @Secured(Roles.S_ADMIN)
    @PostMapping("/{groupId}/add-user/{username}")
    public void addUser(@PathVariable("groupId") String groupId, @PathVariable("username") String username) {
         groupFacadeService.addUser(groupId, username);
    }

    // update
    @Secured(Roles.S_ADMIN)
    @PostMapping("/{groupId}/update")
    public void update(@PathVariable("groupId") String groupId, @Valid @RequestBody GroupRequestDto groupRequestDto) {
         groupFacadeService.update(groupId, groupRequestDto);
    }

    // deleteUser
    @Secured(Roles.S_ADMIN)
    @PostMapping("/{groupId}/delete-user/{userId}")
    public void deleteUser(@PathVariable("groupId") String groupId, @PathVariable("userId") String userId) {
         groupFacadeService.deleteUser(groupId, userId);
    }

    // readGroupTree
    @GetMapping("/{groupId}")
    public GroupTreeDto readGroupTree(@PathVariable("groupId") String groupId) {
         return groupFacadeService.readGroupTree(groupId);
    }

    // getUserList
    @GetMapping("/{groupId}/user-list")
    public List<UserProfileResponseDto> getUserList(@PathVariable("groupId") String groupId) {
         return groupFacadeService.getUserList(groupId);
    }
}