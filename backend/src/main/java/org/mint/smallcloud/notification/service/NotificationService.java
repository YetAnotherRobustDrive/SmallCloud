package org.mint.smallcloud.notification.service;

import org.mint.smallcloud.notification.domain.Notification;
import org.mint.smallcloud.notification.dto.NotificationCountDto;
import org.mint.smallcloud.notification.mapper.NotificationMapper;
import org.mint.smallcloud.notification.repository.NotificationRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationThrowerService notificationThrowerService;
    private final MemberThrowerService memberThrowerService;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationThrowerService notificationThrowerService, MemberThrowerService memberThrowerService, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationThrowerService = notificationThrowerService;
        this.memberThrowerService = memberThrowerService;
        this.notificationMapper = notificationMapper;
    }

    public void delete(Long notificationId, String userName) {
        Member member = memberThrowerService.getMemberByUsername(userName);
        notificationThrowerService.deleteByIdAndOwner(notificationId, member);
    }

    public NotificationCountDto check(String userName) {
        Member member = memberThrowerService.getMemberByUsername(userName);
        List<Notification> notifications = notificationThrowerService.findTop5ByOwnerOrderByLocalDateTimeDesc(member);
        return NotificationCountDto.builder()
            .notificationDtoList(notifications.stream().map(notificationMapper::toNotificationResponseDto).collect(Collectors.toList()))
            .count(notificationRepository.countByOwner(member))
            .build();
    }
}
