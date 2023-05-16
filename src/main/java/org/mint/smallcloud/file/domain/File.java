package org.mint.smallcloud.file.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.share.domain.Share;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "FILES")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class File extends DataNode {
    @Embedded
    private FileLocation location;

    @Column(name = "SIZE")
    private Long size;

    protected File(FileType fileType, FileLocation location, Long size, Long authorId) {
        super(fileType, authorId);
        this.location = location;
        this.size = size;
    }

    public static File of(FileType fileType, FileLocation location, Long size, Member member) {
        return new File(
            fileType,
            location,
            size,
            member.getId()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof File
            && ((File) obj).getId().equals(this.getId());
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