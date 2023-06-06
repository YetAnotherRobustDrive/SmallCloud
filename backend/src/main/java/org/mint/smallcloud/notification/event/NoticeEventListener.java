package org.mint.smallcloud.notification.event;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.notification.domain.Notification;
import org.mint.smallcloud.notification.repository.NotificationRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NoticeEventListener {
    private final NotificationRepository notificationRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void handleNoticeEvent(NoticeEventAfterCommit event) {
        notificationRepository.save(Notification.of(event.getContent(), event.getOwner(), LocalDateTime.now()));
    }
}
