package org.mint.smallcloud.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.notification.service.NotificationService;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@Slf4j
@RequiredArgsConstructor
@Validated
public class NotificationController {
    private final NotificationService notificationService;
    private final UserDetailsProvider userDetailsProvider;
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

    // 알림 확인 여부
}
