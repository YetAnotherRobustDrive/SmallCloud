package org.mint.smallcloud.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.share.domain.MemberShare;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "MEMBERS")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME", nullable = false, length = 15, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 15)
    private String password;

    @Column(name = "NICKNAME", nullable = false, length = 15)
    private String nickname;

    @Column(name = "JOINED_DATE")
    private LocalDateTime joinedDate;

    @Column(name = "CHANGED_PASSWORD_DATE")
    private LocalDateTime changedPasswordDate;

    @Column(name = "IS_LOCKED")
    @Getter(AccessLevel.NONE)
    private boolean isLocked = false;

    @Column(name = "PROFILE_IMAGE_LOCATION")
    private FileLocation profileImageLocation = null;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP")
    private Group group;

    @OneToMany(
        mappedBy = "target",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<MemberShare> shares;

    protected Member(String username, String password, String nickname, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.joinedDate = LocalDateTime.now();
        this.changedPasswordDate = LocalDateTime.now();
    }

    public static Member of(String username, String password, String nickname) {
        return new Member(username, password, nickname, Role.COMMON);
    }

    public static Member createCommon(String username, String password, String nickname) {
        return new Member(username, password, nickname, Role.COMMON);
    }

    public static Member createAdmin(String username, String password, String nickname) {
        return new Member(username, password, nickname, Role.ADMIN);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof Member
            && ((Member) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isRole(Role role) {
        return this.role.equals(role);
    }

    public void unlock() {
        isLocked = false;
    }

    public void lock() {
        isLocked = true;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLoginId(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGroup(Group group) {
        if (getGroup() != null)
            this.group.deleteMember(this);
        this.group = group;
        this.group.addMember(this);
    }

    public void setProfileImageLocation(FileLocation profileImageLocation) {
        this.profileImageLocation = profileImageLocation;
    }
}