package org.mint.smallcloud.file.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.dto.DirectoryCreateDto;
import org.mint.smallcloud.file.dto.DirectoryMoveDto;
import org.mint.smallcloud.file.dto.DirectoryRenameDto;
import org.mint.smallcloud.file.service.DirectoryFacadeService;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        UserDetails user = userDetailsProvider.getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
        directoryFacadeService.create(directoryId, dto, user.getUsername());
    }

    @Secured(Roles.S_COMMON)
    @PostMapping("/{directoryId}/rename")
    public void rename(
        @PathVariable("directoryId") Long directoryId,
        @Valid @RequestBody DirectoryRenameDto dto) {
        UserDetails user = userDetailsProvider.getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
        directoryFacadeService.rename(directoryId, dto, user.getUsername());
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{directoryId}/purge")
    public void purge(@PathVariable("directoryId") Long directoryId) {

    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{directoryId}/restore")
    public void restore(@PathVariable("directoryId") Long directoryId) {

    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{directoryId}/delete")
    public void delete(@PathVariable("directoryId") Long directoryId) {

    }

    @PostMapping("/{directoryId}/move")
    public void move(
        @PathVariable("directoryId") Long directoryId,
        @Valid @RequestBody DirectoryMoveDto dto) {

    }


}
