package org.mint.smallcloud.notification.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "NOTIFICATIONS")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_ID")
    private Long id;

    @Column(name = "CONTENT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private Member owner;

    @Column(name = "LOCAL_DATE_TIME")
    private LocalDateTime localDateTime;

    protected Notification(String content, Member owner, LocalDateTime localDateTime) {
        this.content = content;
        this.owner = owner;
        this.localDateTime = localDateTime;
    }

    public static Notification of(String content, Member owner, LocalDateTime localDateTime) {
        return new Notification(content, owner, localDateTime);
    }
}
