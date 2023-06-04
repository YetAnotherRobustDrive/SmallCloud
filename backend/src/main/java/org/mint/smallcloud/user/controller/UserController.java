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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final MemberFacadeService memberFacadeService;
    private final UserDetailsProvider userDetailsProvider;

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
    public void register(@Valid @RequestBody RegisterDto registerDto) {
        memberFacadeService.register(registerDto);
    }

    @Secured({Roles.S_ADMIN, Roles.S_PRIVILEGE})
    @PostMapping("/{username}/update")
    public void update(
        @UserNameValidation @PathVariable("username") String username,
        @Valid @RequestBody UserProfileRequestDto userProfileDto) {
        memberFacadeService.update(username, userProfileDto);
    }

    @Secured({Roles.S_ADMIN, Roles.S_PRIVILEGE})
    @GetMapping("/{username}")
    public UserProfileResponseDto profile(
        @UserNameValidation @PathVariable("username") String username) {
        return memberFacadeService.profile(username);
    }

    @GetMapping("/search")
    public ResponseDto<List<String>> search(
        @RequestParam("q") String q) {
        userDetailsProvider
            .getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
        List<String> usernames = memberFacadeService.search(q);
        return ResponseDto.<List<String>>builder()
            .result(usernames)
            .build();
    }


    @Secured({Roles.S_COMMON})
    @GetMapping("/root-dir")
    public ResponseDto<Long> getRootDir() {
        return ResponseDto.<Long>builder()
            .result(memberFacadeService
                .getRootDir(userDetailsProvider.getUserDetails()
                    .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername()))
            .build();
    }
}
