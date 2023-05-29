package org.mint.smallcloud.share.controller;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.share.dto.ShareRequestDto;
import org.mint.smallcloud.share.service.ShareService;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/share")
@RequiredArgsConstructor
public class ShareController {
    private final ShareService shareService;
    private final UserDetailsProvider userDetailsProvider;
    // create
    @Secured(Roles.S_COMMON)
    @PostMapping("/create")
    public void create(@Valid @RequestBody ShareRequestDto shareRequestDto) {
        String username = userDetailsProvider
            .getUserDetails().orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        shareService.create(username, shareRequestDto);
    }

    // delete
    @Secured(Roles.S_COMMON)
    @PostMapping("/delete")
    public void delete(@Valid @RequestBody ShareRequestDto shareRequestDto) {
        String username = userDetailsProvider
            .getUserDetails().orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        shareService.delete(username, shareRequestDto);
    }
}
