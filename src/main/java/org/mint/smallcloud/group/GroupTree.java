package org.mint.smallcloud.group;

import java.util.List;

public class GroupTree {
    private final Long id;
    private final String name;
    private final List<GroupTree> children;

    public GroupTree(Long id, String name, List<GroupTree> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }


    public Long getId() { return id; }
    public String getName() { return name; }
    public List<GroupTree> getChildren() { return children; }
}
