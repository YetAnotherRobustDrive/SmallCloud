package org.mint.smallcloud.group;

import org.mint.smallcloud.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class Group {
    private Long id;
    private String name;
    private Group parent;
    private List<Group> children;
    private User manager;
    private List<User> members;

    public static Group of(User manager, String name) { return new Group(); }
    public void addMember(User user) { }
    public List<User> getMembers() { return members; }
    public void setParent(Group parent) { this.parent = parent; }
    public Group getParent() { return parent; }
    public void addChild(Group group) { }
    public void deleteChild(Group group) { }
    public List<Group> getChildren() { return children; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getId() { return id; }
    public GroupTree getSubGroups() {
        return new GroupTree(
            id, name,
            this.children.stream().map(Group::getSubGroups).collect(Collectors.toList())
        );
    }

}
