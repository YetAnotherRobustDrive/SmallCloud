package org.mint.smallcloud.group.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "GROUP_TREES")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GroupTree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_TREE_ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @Column(name = "Y", nullable = false)
    private Integer y;

    @Column(name = "X", nullable = false)
    private Integer x;

    protected GroupTree(Group group, Integer y, Integer x) {
        this.group = group;
        this.y = y;
        this.x = x;
    }

    public static GroupTree of(Group group, Integer y, Integer x) {
        return new GroupTree(group, y, x);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof GroupTree
            && ((GroupTree) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
