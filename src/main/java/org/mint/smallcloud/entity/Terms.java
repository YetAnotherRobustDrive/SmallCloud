package org.mint.smallcloud.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TERMS")
public class Terms {
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "ACTIVATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activatedDate;

    @Lob
    private String content;
}
