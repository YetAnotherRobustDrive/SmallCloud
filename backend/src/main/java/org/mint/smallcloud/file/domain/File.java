package org.mint.smallcloud.file.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    public static File of(Folder parent, FileType fileType, FileLocation location, Long size, Member member) {
        File ret = new File(
            fileType,
            location,
            size,
            member.getId()
        );
        ret.setParentFolder(parent);
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof File
            && ((File) obj).getId().equals(this.getId());
    }


}