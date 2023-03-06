package org.mint.smallcloud.entity;

import javax.persistence.*;

@Table(name = "PROMOTION_REQUESTS")
@Entity
public class PromotionRequestEntity {

    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "MESSAGE")
    @Lob
    private String message;

    @Column(name = "ACCEPTED")
    private Boolean accepted;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    /*
    accepted가 되고 안되고에 따라 여러 request가 생갈 수 있으므로 다대일 관계가 맞다
     */
    @ManyToOne
    @JoinColumn(name = "REVISION_ID")
    private RevisionEntity revision;

}
