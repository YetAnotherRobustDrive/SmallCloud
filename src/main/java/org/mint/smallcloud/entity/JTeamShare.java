package org.mint.smallcloud.entity;

import javax.persistence.*;

@Entity
@Table(name = "J_TEAM_SHARE")
public class JTeamShare {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private TeamEntity team;

    @ManyToOne
    @JoinColumn(name = "SHARE_ID")
    private ShareEntity share;


}
