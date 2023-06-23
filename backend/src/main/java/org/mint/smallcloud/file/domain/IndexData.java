package org.mint.smallcloud.file.domain;

import javax.persistence.*;

@Table(name = "IndexData")
@Entity
public class IndexData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INDEX_DATA_ID")
    private Long id;

    @OneToOne(
        mappedBy = "indexData",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private File originFile;

    private Long size;
    private String location;

    protected IndexData(File originFile, String location, Long size) {
        this.originFile = originFile;
        this.location = location;
        this.size = size;
        originFile.setIndexData(this);
    }

    protected IndexData() {
    }

    public static IndexData of(File originFile, String location, Long size) {
        return new IndexData(originFile, location, size);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof IndexData
            && ((IndexData) obj).getId().equals(this.getId());
    }

    public Long getId() {
        return this.id;
    }

    public File getOriginFile() {
        return this.originFile;
    }

    public Long getSize() {
        return this.size;
    }

    public String getLocation() {
        return this.location;
    }
}
