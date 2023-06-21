package org.mint.smallcloud.file.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "IndexData")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IndexData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INDEX_DATA_ID")
    private Long id;

    @OneToOne(
        mappedBy = "indexData",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    private File originFile;

    private String location;

    public void setOriginFile(File originFile) {
        this.originFile = originFile;
        this.originFile.setIndexData(this);
    }

    protected IndexData(File originFile, String location) {
        setOriginFile(originFile);
        this.location = location;
    }

    public static IndexData of(File originFile, String location) {
        return new IndexData(originFile, location);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof IndexData
            && ((IndexData) obj).getId().equals(this.getId());
    }
}
