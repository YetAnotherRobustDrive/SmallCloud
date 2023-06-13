package org.mint.smallcloud.log.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.log.dto.RequestLogDto;
import org.mint.smallcloud.log.dto.ResponseLogDto;
import org.mint.smallcloud.log.service.LogService;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/logs")
@Slf4j
@RequiredArgsConstructor
@Validated
public class LogController {
    private final LogService logService;

    // 사용자 log 가져오기


    // 관리자 log 가져오기
    @Secured({Roles.S_ADMIN})
    @GetMapping("/admin")
    public List<ResponseLogDto> getAdminLogs(
            @Valid @PathVariable RequestLogDto requestLogDto,
            Pageable pageable) {
        return logService.findLogs(requestLogDto, pageable);
    }
}
