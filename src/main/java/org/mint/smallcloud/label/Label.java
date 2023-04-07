package org.mint.smallcloud.label;

import org.mint.smallcloud.file.File;
import org.mint.smallcloud.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class Label {

    @Id
    @Column(name = "LABEL_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private User owner;

    @ManyToOne(targetEntity = File.class)
    @JoinColumn(name = "FILES")
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