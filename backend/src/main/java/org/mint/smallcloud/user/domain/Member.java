package org.mint.smallcloud.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.share.domain.MemberShare;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "MEMBERS")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME", nullable = false, length = 15, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 60)
    private String password;

    @Column(name = "NICKNAME", nullable = false, length = 15)
    private String nickname;

    @Column(name = "JOINED_DATE")
    private LocalDateTime joinedDate;

    @Column(name = "CHANGED_PASSWORD_DATE")
    private LocalDateTime changedPasswordDate;

    @Column(name = "EXPIRED_DATE")
    private LocalDateTime expiredDate = null;

    @Column(name = "LOCKED")
    private boolean locked = false;

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
    private List<MemberShare> shares = new ArrayList<>();

    protected Member(String username, String password, String nickname, Role role) {
        this.username = username;
        this.password = hashPassword(password);
        this.nickname = nickname;
        this.role = role;
        this.joinedDate = LocalDateTime.now();
        this.changedPasswordDate = LocalDateTime.now();
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static Member of(String username, String password, String nickname) {
        return new Member(username, password, nickname, Role.COMMON);
    }

    public static Member createCommon(String username, String password, String nickname) {
        return new Member(username, password, nickname, Role.COMMON);
    }

    public static Member createAdmin(String username, String password, String nickname) {
        Member ret = new Member(username, password, nickname, Role.ADMIN);
        ret.unlock();
        return ret;
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
        this.password = hashPassword(password);
    }

    public boolean verifyPassword(String password) {
        return passwordEncoder.matches(password, this.password);
    }


    public boolean isRole(Role role) {
        return this.role.equals(role);
    }

    public void unlock() {
        this.locked = false;
    }

    public void lock() {
        this.locked = true;
    }

    public boolean hasGroup() {
        return group != null;
    }

    public String getGroupName() {
        return group == null ? null : group.getName();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeRole(Role role) {
        this.role = role;
    }

    public void setGroup(Group group) {
        if (getGroup() == null) {
            this.group = group;
            if (this.group != null)
                this.group.addMember(this);
            return;
        }
        if (getGroup().equals(group)) return;
        this.group.deleteMember(this);
        this.group = group;
        if (this.group != null)
            this.group.addMember(this);
    }

    public void unsetGroup() {
        setGroup(null);
    }

    public void setProfileImageLocation(FileLocation profileImageLocation) {
        this.profileImageLocation = profileImageLocation;
    }

    public void addShare(MemberShare share) {
        if (!shares.contains(share))
            shares.add(share);
    }

    public void setExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    public boolean canLogin() {
        return !locked && (expiredDate == null || expiredDate.isAfter(LocalDateTime.now()));
    }
}