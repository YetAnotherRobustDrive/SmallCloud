package org.mint.smallcloud.group;

import org.mint.smallcloud.user.User;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "GROUPS")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Group parent;

    @OneToMany(mappedBy = "parent")
    private List<Group> children;

    @OneToMany(mappedBy = "group")
    private List<User> members;

    @ManyToOne
    @JoinColumn(name = "MANAGER_ID")
    private User manager;

    public static Group of(User manager, String name) {
        return new Group();
    }

    public void addMember(User user) {
    }

    public List<User> getMembers() {
        return members;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }

    public Group getParent() {
        return parent;
    }

    public void addChild(Group group) {
    }

    public void deleteChild(Group group) {
    }

    public List<Group> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public GroupTree getSubGroups() {
        return new GroupTree(
            id, name,
            this.children.stream().map(Group::getSubGroups).collect(Collectors.toList())
        );
    }

}