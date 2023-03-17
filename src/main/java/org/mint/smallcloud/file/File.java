package org.mint.smallcloud.file;

import org.mint.smallcloud.data.FileLocation;
import org.mint.smallcloud.group.Group;
import org.mint.smallcloud.label.Label;
import org.mint.smallcloud.share.Share;
import org.mint.smallcloud.user.User;

import java.time.LocalDateTime;
import java.util.*;

public class File {
    private Long id;
    private String description;
    private LocalDateTime createdDate;
    private Long size;
    private FileTypeVO fileType;
    private File folder;
    private FileLocation location;
    private User author;
    private List<Share> Shares;
    private List<Label> labels;

    public Path getFilePath() {
        File cur = folder;
        LinkedList<FolderVO> folders = new LinkedList<>();
        while (cur != null) {
            folders.addFirst(new FolderVO(cur.getId(), cur.getName()));
            cur = cur.folder;
        }
        return new Path(folders);
    }
    public Long getId() { return id; }
    public String getName() { return fileType.getName(); }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public Long getSize() { return size; }
    public FileTypeVO getFileType() { return fileType; }
    public File getFolder() { return folder; }
    public FileLocation getLocation() { return location; }
    public User getAuthor() { return author; }
    public List<Share> getShares() { return Shares; }
    public List<Label> getLabels() { return labels; }

    public void setName(String name) {  }
    public void setDescription(String description) { this.description = description; }
    public void setFileType(FileTypeVO fileType) { this.fileType = fileType; }
    public void setFolder(File folder) { this.folder = folder; }
    public void setLocation(FileLocation location) { this.location = location; }
    public void addShare(Share share) {}
    public void deleteShare(Share share) {}
    public void addLabel(Label label) {}
    public void deleteLabel(Label label) {}

    public void shareToUser(User user) { }
    public void shareToGroup(Group group) { }
    public boolean isOwner(User user) { return true; }
}
