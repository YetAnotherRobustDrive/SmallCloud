package org.mint.smallcloud.file;

import lombok.NoArgsConstructor;
import org.mint.smallcloud.data.FileLocation;
import org.mint.smallcloud.group.Group;
import org.mint.smallcloud.label.Label;
import org.mint.smallcloud.share.Share;
import org.mint.smallcloud.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "FILES")
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "FILE_ID")
    private Long id;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createdDate;

    @Column(name = "SIZE")
    private Long size;

    @Embedded
    private FileType fileType;


    @ManyToOne
    @JoinColumn(name = "FOLDER_ID")
    private Folder folder;

    @Column(name = "LOCATION")
    private FileLocation location;

    @ManyToOne
    @JoinColumn(name = "AUTHOR")
    private User author;

    @OneToMany(mappedBy = "file")
    private List<Share> Shares;

    @ManyToMany
    @JoinTable(
        name = "FILE_LABEL",
        joinColumns = @JoinColumn(name = "FILE_ID"),
        inverseJoinColumns = @JoinColumn(name = "LABEL_ID")
    )
    private List<Label> labels;

//    public Path getFilePath() {
//        File cur = folder;
//        LinkedList<Folder> folders = new LinkedList<>();
//        while (cur != null) {
//            folders.addFirst(new Folder(cur.getId(), cur.getName()));
//            cur = cur.folder;
//        }
//        return new Path(folders);
//    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return fileType.getName();
    }

    //public String getDescription() { return description; }
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Long getSize() {
        return size;
    }

    public FileType getFileType() {
        return fileType;
    }

    //    public File getFolder() { return folder; }
    public FileLocation getLocation() {
        return location;
    }

    public User getAuthor() {
        return author;
    }

    public List<Share> getShares() {
        return Shares;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setName(String name) {
    }

    //public void setDescription(String description) { this.description = description; }
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    //    public void setFolder(File folder) { this.folder = folder; }
    public void setLocation(FileLocation location) {
        this.location = location;
    }

    public void addShare(Share share) {
    }

    public void deleteShare(Share share) {
    }

    public void addLabel(Label label) {
    }

    public void deleteLabel(Label label) {
    }

    public void shareToUser(User user) {
    }

    public void shareToGroup(Group group) {
    }

    public boolean isOwner(User user) {
        return true;
    }
}