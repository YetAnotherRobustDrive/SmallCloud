package org.mint.smallcloud.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VARIANT_TYPE")
public class VariantTypeEntity {
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "variantType")
    private List<VariantEntity> variantEntities = new ArrayList<>();
}
