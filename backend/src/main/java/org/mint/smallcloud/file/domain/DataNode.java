package org.mint.smallcloud.file.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.share.domain.Share;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Table(name = "DATA_NODES")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Getter
public abstract class DataNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DATA_NODE_ID")
    private Long id;

    @Embedded
    private FileType fileType;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID")
    private Member author;

    @ManyToOne
    @JoinColumn(name = "FOLDER_ID")
    private Folder parentFolder;

    @OneToMany(
        mappedBy = "file",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Share> shares = new ArrayList<>();


    @ManyToMany(mappedBy = "files", cascade = CascadeType.ALL)
    private List<Label> labels = new ArrayList<>();

    protected DataNode(FileType fileType, Member author) {
        this.fileType = fileType;
        this.createdDate = LocalDateTime.now();
        this.author = author;
    }

    public static Folder createFolder(Folder parent, String name, Member member) {
        return Folder.of(parent, name, member);
    }

    public static File createFile(Folder parent, FileType filetype, FileLocation location, Long size, Member member) {
        return File.of(parent, filetype, location, size, member);
    }

    public void setParentFolder(Folder folder) {
        if (folder == null) {
            this.parentFolder = null;
            return;
        }
        if (this.parentFolder != null && this.parentFolder.equals(folder)) return;
        this.parentFolder = folder;
        folder.addChild(this);
    }

    public void addShare(Share share) {
        if (share == null || this.shares.contains(share)) return;
        this.shares.add(share);
        share.setFile(this);
    }

    public void deleteShare(Share share) {
        if (share == null || !shares.contains(share)) return;
        shares.remove(share);
        share.setFile(null);
    }

    public void addLabel(Label label) {
        if (label == null || labels.contains(label)) return;
        labels.add(label);
        label.addFile(this);
    }

    public void deleteLabel(Label label) {
        if (label == null || !labels.contains(label)) return;
        labels.remove(label);
        label.deleteFile(this);
    }

    public String getName() {
        return fileType.getName();
    }

    public String getType() {
        return fileType.getType();
    }

    public void setName(String name) {
        this.fileType = FileType.of(name, this.getType());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof DataNode
            && ((DataNode) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public boolean canAccessUser(String username) {
        if (this.getAuthor().getUsername().equals(username))
            return true;
        return getShares().stream().anyMatch(share -> share.canAccess(username));
    }

    public boolean canAccessUser(Member member) {
        return canAccessUser(member.getUsername());
    }

    public boolean isActive() {
        for (Label label : this.getLabels()) {
            System.out.println(label.getName());
            if (label.isTrash()) return false;
        }
        for (Folder folder = parentFolder; folder != null; folder = folder.getParentFolder()) {
            for (Label label : folder.getLabels()) {
                if (label.isTrash()) return false;
            }
        }
        return true;
    }

    public boolean isFile() {
        return this instanceof File;
    }

    public boolean isFolder() {
        return this instanceof Folder;
    }

    public void deleteAllLabel() {
        Iterator<Label> iter = labels.iterator();
        while (iter.hasNext()) {
            Label label = iter.next();
            iter.remove();
            label.deleteFile(this);
        }
    }
}
