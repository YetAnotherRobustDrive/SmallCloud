package org.mint.smallcloud.entity;

import javax.persistence.*;

@Table(name = "J_FILE_FILE_TYPE")
@Entity
public class JFileFileType {
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FILE")
    private FileEntity file;

    @ManyToOne
    @JoinColumn(name = "FILE_TYPE")
    private FileTypeEntity fileType;
}
