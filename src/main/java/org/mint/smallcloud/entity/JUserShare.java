package org.mint.smallcloud.entity;


import javax.persistence.*;

@Entity
@Table(name = "J_USER_SHARE")
public class JUserShare {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "SHARE_ID")
    private ShareEntity share;
}
