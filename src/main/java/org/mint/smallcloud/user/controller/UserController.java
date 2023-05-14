package org.mint.smallcloud.user.controller;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.user.domain.Roles;
import org.mint.smallcloud.user.dto.RegisterDto;
import org.mint.smallcloud.user.dto.UserProfileRequestDto;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
import org.mint.smallcloud.user.service.MemberFacadeService;
import org.mint.smallcloud.validation.UserNameValidation;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final MemberFacadeService memberFacadeService;

    @Secured({Roles.S_ADMIN})
    @RequestMapping(
        value = "/{username}/delete",
        method = {RequestMethod.GET, RequestMethod.POST})
    public void delete(
        @UserNameValidation @PathVariable("username") String username) {
        memberFacadeService.delete(username);
    }

    @Secured({Roles.S_ADMIN})
    @PostMapping
    public void register(@RequestBody RegisterDto registerDto) {
        memberFacadeService.register(registerDto);
    }

    @Secured({Roles.S_ADMIN, Roles.S_PRIVILEGE})
    @PostMapping("/users/{username}/update")
    public void update(
        @UserNameValidation @PathVariable("username") String username,
        @RequestBody UserProfileRequestDto userProfileDto) {
        memberFacadeService.update(username, userProfileDto);
    }

    @Secured({Roles.S_ADMIN, Roles.S_PRIVILEGE})
    @GetMapping("/{username}")
    public UserProfileResponseDto profile(
        @UserNameValidation @PathVariable("username") String username) {
        return memberFacadeService.profile(username);
    }
}
