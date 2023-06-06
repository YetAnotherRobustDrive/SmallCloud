package org.mint.smallcloud.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.notification.domain.Notification;
import org.mint.smallcloud.notification.repository.NotificationRepository;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationThrowerService {
    private final NotificationRepository notificationRepository;
    public void deleteByIdAndOwner(Long notificationId, Member member) {
        if(!notificationRepository.existsByIdAndOwner(notificationId, member))
            throw new ServiceException(ExceptionStatus.NOT_FOUND_NOTIFICATION);
        notificationRepository.deleteByIdAndOwner(notificationId, member);
    }

    public List<Notification> findTop5ByOwnerOrderByLocalDateTimeDesc(Member member) {
        return notificationRepository.findTop5ByOwnerOrderByLocalDateTimeDesc(member);
    }
}
