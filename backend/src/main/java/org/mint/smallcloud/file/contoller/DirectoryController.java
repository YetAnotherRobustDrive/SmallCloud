package org.mint.smallcloud.file.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.dto.*;
import org.mint.smallcloud.file.service.DirectoryFacadeService;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directory")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DirectoryController {
    private final DirectoryFacadeService directoryFacadeService;
    private final UserDetailsProvider userDetailsProvider;

    @Secured(Roles.S_COMMON)
    @PostMapping("/{directoryId}/create")
    public void create(
        @PathVariable("directoryId") Long directoryId,
        @Valid @RequestBody DirectoryCreateDto dto) {
        UserDetails user = getLoginUser();
        directoryFacadeService.create(directoryId, dto, user.getUsername());
    }

    @Secured(Roles.S_COMMON)
    @PostMapping("/{directoryId}/rename")
    public void rename(
        @PathVariable("directoryId") Long directoryId,
        @Valid @RequestBody DirectoryRenameDto dto) {
        UserDetails user = getLoginUser();
        directoryFacadeService.rename(directoryId, dto, user.getUsername());
    }

    // TODO: 여기 아래 3개의 메서드는 라벨이 있어야 구현 가능하다. 이후 구현하자

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{directoryId}/purge")
    public void purge(@PathVariable("directoryId") Long directoryId) {

    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{directoryId}/restore")
    public void restore(@PathVariable("directoryId") Long directoryId) {

    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{directoryId}/delete")
    public void delete(@PathVariable("directoryId") Long directoryId) {

    }

    @Secured(Roles.S_COMMON)
    @PostMapping("/{directoryId}/move")
    public void move(
        @PathVariable("directoryId") Long directoryId,
        @Valid @RequestBody DirectoryMoveDto dto) {
        UserDetails user = getLoginUser();
        directoryFacadeService.move(directoryId, dto, user.getUsername());
    }

    @Secured({Roles.S_COMMON})
    @GetMapping("/{directoryId}")
    public DirectoryDto info(@PathVariable("directoryId") Long directoryId) {
        UserDetails user = getLoginUser();
        return directoryFacadeService.info(directoryId, user.getUsername());
    }

    @Secured(Roles.S_COMMON)
    @GetMapping("/{directoryId}/subDirectories")
    public List<DirectoryDto> subDirectories(@PathVariable("directoryId") Long directoryId) {
        UserDetails user = getLoginUser();
        return directoryFacadeService.subDirectories(directoryId, user.getUsername());
    }

    @Secured(Roles.S_COMMON)
    @GetMapping("/{directoryId}/files")
    public List<FileDto> files(@PathVariable("directoryId") Long directoryId) {
        UserDetails user = getLoginUser();
        return directoryFacadeService.files(directoryId, user.getUsername());
    }

    private UserDetails getLoginUser() {
        return userDetailsProvider.getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
    }
}
