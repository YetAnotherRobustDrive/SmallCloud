package org.mint.smallcloud.group.domain;

import org.mint.smallcloud.share.domain.GroupShare;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "GROUPS")
@Entity
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

    @OneToMany(
        mappedBy = "parentGroup",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Group> subGroups = new ArrayList<>();

    @OneToMany(
        mappedBy = "target",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<GroupShare> shares = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Member> members = new ArrayList<>();

    protected Group(String name, Group parentGroup) {
        this.name = name;
        this.parentGroup = parentGroup;
    }

    protected Group() {
    }

    public static Group of(String name, Group parent) {
        return new Group(name, parent);
    }

    public static Group of(String name) {
        return new Group(name, null);
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

    public void addMember(Member member) {
        if (!this.members.contains(member)) {
            this.members.add(member);
            member.setGroup(this);
        }
    }

    public void deleteMember(Member member) {
        if (this.members.contains(member)) {
            this.members.remove(member);
            member.setGroup(null);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentGroup(Group parentGroup) {
        if (getParentGroup() == null) {
            this.parentGroup = parentGroup;
            if (parentGroup != null)
                parentGroup.addSubGroup(this);
            return;
        }
        if (getParentGroup().equals(parentGroup)) return;
        this.parentGroup.deleteSubGroup(this);
        this.parentGroup = parentGroup;
        if (parentGroup != null)
            parentGroup.addSubGroup(this);
    }

    public void addSubGroup(Group subGroup) {
        if (!this.subGroups.contains(subGroup)) {
            this.subGroups.add(subGroup);
            subGroup.setParentGroup(this);
        }
    }

    public void deleteSubGroup(Group subGroup) {
        if (this.subGroups.contains(subGroup)) {
            this.subGroups.remove(subGroup);
            subGroup.setParentGroup(null);
        }
    }

    public void addShare(GroupShare rst) {
        if (!getShares().contains(rst))
            getShares().add(rst);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Group getParentGroup() {
        return this.parentGroup;
    }

    public List<Group> getSubGroups() {
        return this.subGroups;
    }

    public List<GroupShare> getShares() {
        return this.shares;
    }

    public List<Member> getMembers() {
        return this.members;
    }
}