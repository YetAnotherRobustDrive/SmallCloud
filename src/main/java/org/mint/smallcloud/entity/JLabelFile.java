package org.mint.smallcloud.entity;

import javax.persistence.*;

@Entity
@Table(name = "J_LABEL_FILE")
public class JLabelFile {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "LABEL_ID")
    private LabelEntity label;

    @ManyToOne
    @JoinColumn(name = "FILE_ID")
    private FileEntity file;

}
