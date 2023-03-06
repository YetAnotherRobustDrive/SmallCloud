package org.mint.smallcloud.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "LABELS")
public class LabelEntity {
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @OneToMany(mappedBy = "label")
    private List<JLabelFile> jLabelFile;
}
