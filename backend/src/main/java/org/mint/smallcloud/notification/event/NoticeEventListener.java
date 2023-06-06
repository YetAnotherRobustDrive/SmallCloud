package org.mint.smallcloud.notification.event;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.notification.domain.Notification;
import org.mint.smallcloud.notification.repository.NotificationRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.domain.Role;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NoticeEventListener {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void handleNoticeEvent(NoticeEventAfterCommit event) {
        notificationRepository.save(Notification.of(event.getContent(), event.getOwner(), LocalDateTime.now()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void handleNoticeAllEvent(NoticeAllEventAfterCommit event) {
        List<Member> members = memberRepository.findMembersByRole(Role.COMMON);
        members.forEach(member ->
            notificationRepository.save(
                Notification.of(event.getContent(), member, LocalDateTime.now())));
    }
}
