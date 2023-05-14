package org.mint.smallcloud.group.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.share.domain.GroupShare;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;
import java.util.List;

@Table(name = "GROUPS")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 15, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "PARENT_GROUP_ID")
    private Group parentGroup;

    @ManyToOne
    @JoinColumn(name = "MANAGER_ID")
    private Member manager;

    @OneToMany(
        mappedBy = "parentGroup",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Group> subGroups;

    @OneToMany(
        mappedBy = "target",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<GroupShare> shares;

    @OneToMany(mappedBy = "group")
    private List<Member> members;

    public Group(String name, Member manager, Group parentGroup) {
        this.name = name;
        this.manager = manager;
        this.parentGroup = parentGroup;
        if (this.manager != null) {
            addMember(manager);
        }
    }

    public static Group of(String name, Member member, Group parent) {
        return new Group(name, member, parent);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof Group
            && ((Group) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /* TODO: */
    public void addMember(Member member) {
    }

    public void deleteMember(Member member) {
    }
}