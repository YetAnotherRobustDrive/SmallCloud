package org.mint.smallcloud.notification.service;

import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.notification.domain.Notification;
import org.mint.smallcloud.notification.repository.NotificationRepository;
import org.mint.smallcloud.user.domain.Member;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationThrowerService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(NotificationThrowerService.class);
    private final NotificationRepository notificationRepository;

    public NotificationThrowerService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void deleteByIdAndOwner(Long notificationId, Member member) {
        if (!notificationRepository.existsByIdAndOwner(notificationId, member))
            throw new ServiceException(ExceptionStatus.NOT_FOUND_NOTIFICATION);
        notificationRepository.deleteByIdAndOwner(notificationId, member);
    }

    public List<Notification> findTop5ByOwnerOrderByLocalDateTimeDesc(Member member) {
        return notificationRepository.findTop5ByOwnerOrderByLocalDateTimeDesc(member);
    }
}
