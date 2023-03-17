package org.mint.smallcloud.user;

import org.mint.smallcloud.data.FileLocation;
import org.mint.smallcloud.file.File;
import org.mint.smallcloud.group.Group;
import org.mint.smallcloud.label.Label;

import java.time.LocalDateTime;
import java.util.List;

public class User {
    private Long id;
    private String loginId;
    private String loginPw;
    private String nickname;
    private LocalDateTime joinedDate;
    private boolean isLocked;
    private LocalDateTime changedPwDate;
    private Group group;
    private FileLocation profileImageLocation;
    private List<File> files;
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
