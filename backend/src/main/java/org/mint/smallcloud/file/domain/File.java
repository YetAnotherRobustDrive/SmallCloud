package org.mint.smallcloud.file.domain;

import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;
import java.util.List;

@Table(name = "FILES")
@Entity
public class File extends DataNode {
    @Embedded
    private FileLocation location;

    @Column(name = "SIZE")
    private Long size;

    @OneToMany(
        mappedBy = "file",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Segment> segments;

    @OneToOne
    @JoinColumn(name = "INDEX_DATA_ID")
    private IndexData indexData = null;

    protected File(FileType fileType, FileLocation location, Long size, Member member) {
        super(fileType, member);
        this.location = location;
        this.size = size;
    }

    protected File() {
    }

    public static File of(Folder parent, FileType fileType, FileLocation location, Long size, Member member) {
        File ret = new File(
            fileType,
            location,
            size,
            member
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

    public void updateContents(String newLoc, Long newSize) {
        location = FileLocation.of(newLoc);
        this.size = newSize;
    }

    public void setIndexData(IndexData indexData) {
        this.indexData = indexData;
    }

    public boolean isEncoded() {
        return this.indexData != null;
    }

    public FileLocation getLocation() {
        return this.location;
    }

    public Long getSize() {
        return this.size;
    }

    public List<Segment> getSegments() {
        return this.segments;
    }

    public IndexData getIndexData() {
        return this.indexData;
    }
}
