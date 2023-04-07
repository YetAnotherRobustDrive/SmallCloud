package org.mint.smallcloud.user;

import lombok.*;
import org.mint.smallcloud.data.FileLocation;
import org.mint.smallcloud.file.File;
import org.mint.smallcloud.group.Group;
import org.mint.smallcloud.label.Label;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class User {
    @Id
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "LOGIN_ID", nullable = false, length = 15, unique = true)
    private String loginId;

    @Column(name = "LOGIN_PW", nullable = false, length = 15)
    private String loginPw;

    @Column(name = "NICKNAME", nullable = false, length = 15)
    private String nickname;

    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "JOINED_DATE")
    private LocalDateTime joinedDate;

    @Column(name = "IS_LOCKED")
    private boolean isLocked;

    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHANGE_PW_DATE")
    private LocalDateTime changedPwDate;

    @ManyToOne
    @JoinColumn(name = "GROUP")
    private Group group;

    @Column(name = "PROFILE_IMAGE_LOCATION")
    private FileLocation profileImageLocation;

    @OneToMany(mappedBy = "author")
    @Column(name = "FILES")
    private List<File> files;

    @OneToMany(mappedBy = "owner")
    @Column(name = "LABELS")
    private List<Label> labels;

    static User of(String loginId, String loginPw, String nickname) { return new User(); }

    public void changePassword(String oldPassword, String newPassword) { }
    public boolean verifyPassword(String password) { return true; }
    public void unlock() { }
    public void lock() { }
    public boolean isLocked() { return true; }

    public void setLoginId(String loginId) { this.loginId = loginId; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setGroup(Group group) { this.group = group; }
    public void setProfileImageLocation(FileLocation profileImageLocation) { this.profileImageLocation = profileImageLocation; }

    public Long getId() { return id; }
    public String getLoginId() { return loginId; }
    public String getNickname() { return nickname; }
    public LocalDateTime getJoinedDate() { return joinedDate; }
    public LocalDateTime getChangedPwDate() { return changedPwDate; }
    public Group getGroup() { return group; }
    public FileLocation getProfileImageLocation() { return profileImageLocation; }

    public void addFile(File file) { }
    public void deleteFile(File file) { }
    public List<File> getFiles() { return files; }
}