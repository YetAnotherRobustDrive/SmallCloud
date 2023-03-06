package org.mint.smallcloud.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "REVISIONS")
public class RevisionEntity {

    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "FILE_ADDR", nullable = false)
    private String fileAddr;

    @ManyToOne
    @JoinColumn(name = "FILE_ID")
    private FileEntity file;

    @OneToMany(mappedBy = "revision")
    private List<VariantEntity> variantEntities = new ArrayList<>();
}
