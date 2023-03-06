package org.mint.smallcloud.entity;


import javax.persistence.*;

@Entity
@Table(name = "VARIANTS")
public class VariantEntity {
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "ADDR", length = 255)
    private String addr;

    @ManyToOne
    @JoinColumn(name = "REVISION_ID")
    private RevisionEntity revision;

    @ManyToOne
    @JoinColumn(name = "VARIANT_TYPE_ID")
    private VariantTypeEntity variantType;

}
