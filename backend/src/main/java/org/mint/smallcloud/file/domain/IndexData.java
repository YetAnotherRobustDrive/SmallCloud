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

    @OneToOne(mappedBy = "indexData")
    private File originFile;

    private String location;

    protected IndexData(File originFile, String location) {
        this.originFile = originFile;
        this.location = location;
    }

    public static IndexData of(File originFile, String location) {
        return new IndexData(originFile, location);
    }
}
