package org.mint.smallcloud.label.controller;

import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.label.dto.LabelFilesDto;
import org.mint.smallcloud.label.service.LabelService;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Roles;
import org.slf4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/labels")
@Validated
public class LabelController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LabelController.class);
    private final LabelService labelService;
    private final UserDetailsProvider userDetailsProvider;

    public LabelController(LabelService labelService, UserDetailsProvider userDetailsProvider) {
        this.labelService = labelService;
        this.userDetailsProvider = userDetailsProvider;
    }

    // 라벨 검색
    @Secured({Roles.S_COMMON})
    @GetMapping("/search")
    public LabelFilesDto search(@RequestParam("labelName") String labelName) {
        String userName = userDetailsProvider.getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        return labelService.search(labelName, userName);
    }

    // 휴지통 라벨 리스트 검색
    @Secured({Roles.S_COMMON})
    @GetMapping("/trash")
    public LabelFilesDto trash() {
        String userName = userDetailsProvider.getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        return labelService.trash(userName);
    }

    // 즐겨찾기 라벨 리스트 검색
    @Secured({Roles.S_COMMON})
    @GetMapping("/favorite")
    public LabelFilesDto favorite() {
        String userName = userDetailsProvider.getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        return labelService.favorite(userName);
    }
}
