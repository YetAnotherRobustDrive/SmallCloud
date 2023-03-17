package org.mint.smallcloud.label;

import org.mint.smallcloud.file.File;
import org.mint.smallcloud.user.User;

import java.util.List;

public class Label {
    private Long id;
    private String name;
    private User owner;
    private List<File> files;

    public static Label of(User user, String name) {
        return new Label();
    }

    public void addFile(File file){ }
    public void setName(String name) { this.name = name; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public User getOwner() { return owner; }
    public List<File> getFiles() { return files; }
}
