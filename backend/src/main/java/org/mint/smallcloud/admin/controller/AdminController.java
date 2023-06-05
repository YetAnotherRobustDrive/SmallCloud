package org.mint.smallcloud.admin.controller;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.admin.dto.ChangePasswordDto;
import org.mint.smallcloud.admin.service.AdminService;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
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
    public void resetPassword(@PathVariable String username, @Valid @RequestBody ChangePasswordDto dto) {
        adminService.resetPassword(username, dto);
    }
}
