package org.mint.smallcloud.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "SHARES")
public class ShareEntity {
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRE_DATE")
    private Date expireDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "SHARE_TYPE")
    ShareType shareType;

    @ManyToOne
    @JoinColumn(name = "REVISION_ID")
    RevisionEntity revision;

    @OneToMany(mappedBy = "share")
    private List<JUserShare> jUserShare = new ArrayList<>();

    @OneToMany(mappedBy = "share")
    private List<JTeamShare> jTeamShare = new ArrayList<>();
}
