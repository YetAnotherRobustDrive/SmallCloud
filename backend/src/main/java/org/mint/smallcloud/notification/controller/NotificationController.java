package org.mint.smallcloud.notification.controller;

import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.notification.dto.NotificationCountDto;
import org.mint.smallcloud.notification.service.NotificationService;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Roles;
import org.slf4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@Validated
public class NotificationController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;
    private final UserDetailsProvider userDetailsProvider;

    public NotificationController(NotificationService notificationService, UserDetailsProvider userDetailsProvider) {
        this.notificationService = notificationService;
        this.userDetailsProvider = userDetailsProvider;
    }

    // 알림 삭제
    @Secured({Roles.S_COMMON})
    @RequestMapping(
        value = "/{notificationId}/delete",
        method = {RequestMethod.GET, RequestMethod.POST})
    public void delete(@PathVariable("notificationId") Long notificationId) {
        String userName = userDetailsProvider.getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        notificationService.delete(notificationId, userName);
    }

    // 알림 확인
    @Secured({Roles.S_COMMON})
    @GetMapping("/{userName}/confirm")
    public NotificationCountDto check(@PathVariable("userName") String userName) {
        return notificationService.check(userName);
    }
}
