package org.mint.smallcloud.file.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.share.domain.Share;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "AUTHOR_ID")
    private Long authorId;

    @ManyToOne
    @JoinColumn(name = "FOLDER_ID")
    private Folder parentFolder;

    @OneToMany(
        mappedBy = "file",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Share> shares;


    @ManyToMany(mappedBy = "labels")
    private List<Label> labels;

    protected DataNode(FileType fileType, Long authorId) {
        this.fileType = fileType;
        this.createdDate = LocalDateTime.now();
        this.authorId = authorId;
    }

    public static DataNode createFolder(Folder parent, String name, Member member) {
        return Folder.of(parent, name, member);
    }

    public static DataNode createFile(Folder parent, FileType filetype, FileLocation location, Long size, Member member) {
        return File.of(parent, filetype, location, size, member);
    }

    public void setParentFolder(Folder folder) {
        if (this.parentFolder.equals(folder)) return;
        this.parentFolder = folder;
        folder.addChild(this);
    }

    public void addShare(Share share) {
        if (shares.contains(share)) return;
        shares.add(share);
        share.setFile(this);
    }

    public void deleteShare(Share share) {
        if (!shares.contains(share)) return;
        shares.remove(share);
        share.setFile(null);
    }

    public void addLabel(Label label) {
        if (labels.contains(label)) return;
        labels.add(label);
        label.addFile(this);
    }

    public void deleteLabel(Label label) {
        if (!labels.contains(label)) return;
        labels.remove(label);
        label.deleteFile(this);
    }

    public String getName() {
        return fileType.getName();
    }

    public String getType() {
        return fileType.getType();
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
}
