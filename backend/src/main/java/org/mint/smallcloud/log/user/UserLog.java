package org.mint.smallcloud.log.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "USER_LOG")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserLog {
    @Id
    @Column(name = "SHARE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "TIME")
    private LocalDateTime time;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "ID_ADDR")
    private String ipAddr;

    @Column(name = "STATUS")
    private Boolean status;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof org.mint.smallcloud.share.domain.GroupShare
            && ((org.mint.smallcloud.share.domain.GroupShare) obj).getId().equals(this.getId());
    }

    protected UserLog(Member member, LocalDateTime time, String action, String ipAddr, Boolean status) {
        this.member = member;
        this.time = time;
        this.action = action;
        this.ipAddr = ipAddr;
        this.status = status;
    }

    public static UserLog of(Member member, LocalDateTime time, String action, String ipAddr, Boolean status) {
        return new UserLog(member, time, action, ipAddr, status);
    }

    @Override
    public String toString() {
        String name = member == null ? "null" : member.getUsername();
        return "UserLog{" +
            "id=" + id +
            ", member=" + name +
            ", time=" + time +
            ", action='" + action + '\'' +
            '}';
    }
}
