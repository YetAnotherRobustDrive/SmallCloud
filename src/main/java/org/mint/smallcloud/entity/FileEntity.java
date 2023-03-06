package org.mint.smallcloud.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FILES")
public class FileEntity {
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "SIZE", nullable = false)
    private Long size;

    @ManyToOne
    @JoinColumn(name = "USER")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "TEAM")
    private TeamEntity team;

    @OneToMany(mappedBy = "file")
    private List<JLabelFile> jLabelFile;

    @OneToMany(mappedBy = "file")
    private List<JFileFileType> jFileFileType = new ArrayList<>();
}
