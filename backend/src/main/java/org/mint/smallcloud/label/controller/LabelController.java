package org.mint.smallcloud.label.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.label.dto.LabelFilesDto;
import org.mint.smallcloud.label.service.LabelService;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/labels")
@Slf4j
@RequiredArgsConstructor
@Validated
public class LabelController {
    private final LabelService labelService;
    private final UserDetailsProvider userDetailsProvider;

    // 라벨 검색
    @Secured({Roles.S_COMMON})
    @GetMapping("/search")
    public LabelFilesDto search(@RequestParam("labelName") String labelName) {
        String userName = userDetailsProvider.getUserDetails()
                .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        return labelService.search(labelName, userName);
    }
}
