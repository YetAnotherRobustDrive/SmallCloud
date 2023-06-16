package org.mint.smallcloud.admin.controller;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.admin.domain.AdminConfig;
import org.mint.smallcloud.admin.dto.AdminConfigDto;
import org.mint.smallcloud.admin.dto.ChangePasswordDto;
import org.mint.smallcloud.admin.service.AdminService;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {
    private final AdminService adminService;

    @Secured(Roles.S_ADMIN)
    @PostMapping("/lock/{username}")
    public void lockUser(@PathVariable String username) {
        adminService.lockUser(username, true);
    }

    @Secured(Roles.S_ADMIN)
    @PostMapping("/unlock/{username}")
    public void unlockUser(@PathVariable String username) {
        adminService.lockUser(username, false);
    }

    @Secured(Roles.S_ADMIN)
    @PostMapping("/change-password/{username}")
    public void resetPassword(
        @RequestBody ChangePasswordDto dto,
        @PathVariable String username
    ) {
        adminService.resetPassword(username, dto);
    }

    @Secured(Roles.S_ADMIN)
    @PostMapping("/config")
    public void nickname(@RequestBody AdminConfigDto adminConfigDto) {
        adminService.requestConfig(adminConfigDto);
    }

    @GetMapping("/config")
    public AdminConfig nickname(@PathVariable String code) {
        return adminService.responseConfig(code);
    }
}
