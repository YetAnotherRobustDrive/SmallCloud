package org.mint.smallcloud.user.controller;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.user.domain.Roles;
import org.mint.smallcloud.user.dto.RegisterDto;
import org.mint.smallcloud.user.dto.UserProfileRequestDto;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
import org.mint.smallcloud.user.service.MemberFacadeService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final MemberFacadeService memberFacadeService;

    @Secured({Roles.S_ADMIN})
    @RequestMapping(
        value = "/{username}/delete",
        method = {RequestMethod.GET, RequestMethod.POST})
    public void delete(@PathVariable("username") String username) {
        memberFacadeService.delete(username);
    }

    @Secured({Roles.S_ADMIN})
    @PostMapping
    public void register(@RequestBody RegisterDto registerDto) {
        memberFacadeService.register(registerDto);
    }

    @Secured({Roles.S_ADMIN, Roles.S_PRIVILEGE})
    @PostMapping("/users/{username}/update")
    public void update(@PathVariable("username") String username, @RequestBody UserProfileRequestDto userProfileDto) {
        memberFacadeService.update(username, userProfileDto);
    }

    @Secured({Roles.S_ADMIN, Roles.S_PRIVILEGE})
    @GetMapping("/{username}")
    public UserProfileResponseDto profile(
        @Size(min = 1, max = 15, message = "아이디는 15자 이하로 작성해 주세요")
        @NotBlank(message = "아이디는 필수로 들어가야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]+", message = "아이디는 영어나 숫자만 가능합니다.")
        @PathVariable("username")
        String username) {
        return memberFacadeService.profile(username);
    }
}
