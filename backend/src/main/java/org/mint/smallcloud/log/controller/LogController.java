package org.mint.smallcloud.log.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.log.dto.RequestLogDto;
import org.mint.smallcloud.log.dto.ResponseLogDto;
import org.mint.smallcloud.log.dto.ResponseLoginLogDto;
import org.mint.smallcloud.log.service.LogService;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/logs")
@Slf4j
@RequiredArgsConstructor
@Validated
public class LogController {
    private final LogService logService;
    private final UserDetailsProvider userDetailsProvider;


    // 사용자 log 가져오기
    @Secured({Roles.S_COMMON})
    @GetMapping("")
    public List<ResponseLoginLogDto> getLoginLogs() {
        UserDetails user = getLoginUser();
        return logService.findLoginLogsByUser(user.getUsername());
    }


    private UserDetails getLoginUser() {
        return userDetailsProvider.getUserDetails()
                .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
    }

    // 관리자 log 가져오기
    @Secured({Roles.S_ADMIN})
    @GetMapping("/admin")
    public List<ResponseLogDto> getAdminLogs(
            @Valid @RequestBody RequestLogDto requestLogDto, Pageable pageable) {
        return logService.findLogs(requestLogDto, pageable);
    }
}
