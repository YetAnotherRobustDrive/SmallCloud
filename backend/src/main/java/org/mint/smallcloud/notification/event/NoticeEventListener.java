package org.mint.smallcloud.notification.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.notification.domain.Notification;
import org.mint.smallcloud.notification.repository.NotificationRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.domain.Role;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NoticeEventListener {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleNoticeEvent(NoticeEventAfterCommit event) {
        notificationRepository.save(Notification.of(event.getContent(), event.getOwner(), LocalDateTime.now()));
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleNoticeAllEvent(NoticeAllEventAfterCommit event) {
        List<Member> members = memberRepository.findMembersByRole(Role.COMMON);
        members.forEach(member ->
            notificationRepository.save(
                Notification.of(event.getContent(), member, LocalDateTime.now())));
    }
}
