package org.mint.smallcloud.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "USERS")
@Getter @Setter
public class UserEntity {
    @Id @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "LOGIN_ID", length = 15, nullable = false, unique = true)
    private String loginId;

    @Column(name = "LOGIN_PW", length = 20, nullable = false)
    private String loginPw;

    @Column(name = "NICKNAME", length = 15, nullable = false, unique = true)
    private String nickname;

    @Column(name = "CREATED_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "CHANGED_PW_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date changedPwDate;

    @Column(name = "IS_LOCKED", nullable = false)
    private boolean isLocked;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private TeamEntity team;

    @OneToMany(mappedBy = "user")
    private List<JUserShare> jUserShare = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<LabelEntity> labelEntities = new ArrayList<>();
}
