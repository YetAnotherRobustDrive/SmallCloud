package org.mint.smallcloud.file.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.share.domain.Share;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "FILES")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    private Long id;

    @Embedded
    private FileType fileType;

    @Embedded
    private FileLocation location;

    @Column(name = "SIZE")
    private Long size;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createdDate;

    @Column(name = "AUTHOR_ID")
    private Long authorId;

    @ManyToOne
    @JoinColumn(name = "FOLDER_ID")
    private Folder folder;

    @OneToMany(
        mappedBy = "file",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Share> Shares;

    @ManyToMany
    @JoinTable(
        name = "FILE_LABEL",
        joinColumns = @JoinColumn(name = "FILE_ID"),
        inverseJoinColumns = @JoinColumn(name = "LABEL_ID")
    )
    private List<Label> labels;

    protected File(FileType fileType, FileLocation location, Long size, Long authorId, Folder folder) {
        this.size = size;
        this.fileType = fileType;
        this.location = location;
        this.authorId = authorId;
        this.folder = folder;
        createdDate = LocalDateTime.now();
    }

    public static File of(String fileName, String fileType, String location, Long size, Long authorId, Folder folder) {
        return new File(
            FileType.of(fileName, fileType),
            FileLocation.of(location),
            size,
            authorId,
            folder
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof File
            && ((File) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /* TODO: */
    public void addShare(Share share) {
    }

    public void deleteShare(Share share) {
    }

    public void addLabel(Label label) {
    }

    public void deleteLabel(Label label) {
    }

    public void shareToUser(Member member) {
    }

    public void shareToGroup(Group group) {
    }
    /* TODO */
}