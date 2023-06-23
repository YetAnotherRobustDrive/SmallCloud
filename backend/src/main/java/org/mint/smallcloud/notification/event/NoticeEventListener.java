package org.mint.smallcloud.notification.event;

import org.mint.smallcloud.notification.domain.Notification;
import org.mint.smallcloud.notification.repository.NotificationRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.domain.Role;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NoticeEventListener {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(NoticeEventListener.class);
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public NoticeEventListener(NotificationRepository notificationRepository, MemberRepository memberRepository) {
        this.notificationRepository = notificationRepository;
        this.memberRepository = memberRepository;
    }

    @EventListener
    @Transactional
    public void handleNoticeEvent(NoticeEventAfterCommit event) {
        notificationRepository.save(Notification.of(event.getContent(), event.getOwner(), LocalDateTime.now()));
    }

    @EventListener
    @Transactional
    public void handleNoticeAllEvent(NoticeAllEventAfterCommit event) {
        List<Member> members = memberRepository.findMembersByRole(Role.COMMON);
        members.forEach(member ->
            notificationRepository.save(
                Notification.of(event.getContent(), member, LocalDateTime.now())));
    }
}
