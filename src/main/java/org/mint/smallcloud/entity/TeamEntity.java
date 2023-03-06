package org.mint.smallcloud.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TEAMS")
@Getter @Setter
public class TeamEntity {
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true, nullable = false, length = 15)
    private String name;

    @OneToMany(mappedBy = "team")
    private List<UserEntity> users = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "PARENT")
    private TeamEntity parent;

    @OneToMany(mappedBy = "team")
    private List<JTeamShare> jTeamShare = new ArrayList<>();
}
