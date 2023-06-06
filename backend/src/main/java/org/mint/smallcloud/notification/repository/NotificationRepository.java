package org.mint.smallcloud.notification.repository;

import org.mint.smallcloud.notification.domain.Notification;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    void deleteByIdAndOwner(Long id, Member owner);
    List<Notification> findTop5ByOwnerOrderByLocalDateTimeDesc(Member owner);
    Long countByOwner(Member owner);
    boolean existsByIdAndOwner(Long id, Member owner);
}
