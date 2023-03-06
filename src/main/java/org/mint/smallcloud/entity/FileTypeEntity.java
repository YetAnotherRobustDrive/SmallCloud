package org.mint.smallcloud.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FILE_TYPE")
public class FileTypeEntity {
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "name", length = 10)
    private String name;

    @OneToMany(mappedBy = "fileType")
    private List<JFileFileType> jFileFileType = new ArrayList<>();
}
