package org.mint.smallcloud.group.controller;

import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.group.dto.GroupRequestDto;
import org.mint.smallcloud.group.dto.GroupTreeDto;
import org.mint.smallcloud.group.service.GroupFacadeService;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Roles;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {
    private final GroupFacadeService groupFacadeService;
    private final UserDetailsProvider userDetailsProvider;

    public GroupController(GroupFacadeService groupFacadeService, UserDetailsProvider userDetailsProvider) {
        this.groupFacadeService = groupFacadeService;
        this.userDetailsProvider = userDetailsProvider;
    }

    // create
    @Secured(Roles.S_ADMIN)
    @PostMapping("/create")
    public void create(@Valid @RequestBody GroupRequestDto groupRequestDto) {
        groupFacadeService.create(groupRequestDto);
    }


    @Secured(Roles.S_ADMIN)
    @RequestMapping(value = "/{groupName}/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public void delete(@PathVariable("groupName") String groupName) {
        groupFacadeService.delete(groupName);
    }

    // addUser
    @Secured(Roles.S_ADMIN)
    @PostMapping("/{groupName}/add-user/{username}")
    public void addUser(@PathVariable("groupName") String groupName, @PathVariable("username") String username) {
        groupFacadeService.addUser(groupName, username);
    }

    // update
    @Secured(Roles.S_ADMIN)
    @PostMapping("/{groupName}/update")
    public void update(@PathVariable("groupName") String groupName, @Valid @RequestBody GroupRequestDto groupRequestDto) {
        groupFacadeService.update(groupName, groupRequestDto);
    }

    // deleteUser
    @Secured(Roles.S_ADMIN)
    @PostMapping("/{groupName}/delete-user/{username}")
    public void deleteUser(@PathVariable("groupName") String groupName, @PathVariable("username") String username) {
        groupFacadeService.deleteUser(groupName, username);
    }

    // readGroupTree
    @GetMapping("/{groupName}")
    public GroupTreeDto readGroupTree(@PathVariable("groupName") String groupName) {
        return groupFacadeService.readGroupTree(groupName);
    }

    // getUserList
    @GetMapping("/{groupName}/user-list")
    public List<UserProfileResponseDto> getUserList(@PathVariable("groupName") String groupName) {
        return groupFacadeService.getUserList(groupName);
    }

    @GetMapping("/search")
    public List<String> search(@RequestParam("q") String keyword) {
        userDetailsProvider
            .getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
        return groupFacadeService.search(keyword);
    }
}
