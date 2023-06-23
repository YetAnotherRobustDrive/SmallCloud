package org.mint.smallcloud.share.controller;

import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.dto.DirectoryDto;
import org.mint.smallcloud.file.dto.FileDto;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.share.dto.ShareRequestDto;
import org.mint.smallcloud.share.service.ShareService;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/share")
public class ShareController {
    private final ShareService shareService;
    private final UserDetailsProvider userDetailsProvider;

    public ShareController(ShareService shareService, UserDetailsProvider userDetailsProvider) {
        this.shareService = shareService;
        this.userDetailsProvider = userDetailsProvider;
    }

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

    @Secured(Roles.S_COMMON)
    @GetMapping("/file-list")
    public List<FileDto> getFileList() {
        String username = userDetailsProvider
            .getUserDetails().orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        return shareService.getFileList(username);
    }

    @Secured(Roles.S_COMMON)
    @GetMapping("/directory-list")
    public List<DirectoryDto> getDirectoryList() {
        String username = userDetailsProvider
            .getUserDetails().orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        return shareService.getDirectoryList(username);
    }
}
