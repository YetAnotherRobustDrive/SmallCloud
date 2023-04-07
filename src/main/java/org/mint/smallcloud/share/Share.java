package org.mint.smallcloud.share;

import org.mint.smallcloud.file.File;
import org.mint.smallcloud.user.User;

import javax.persistence.*;

@Entity
public abstract class Share {

    @Id
    @Column(name = "SHARE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FILE_ID")
    @Column(name = "FILE")
    private File file;

    public Long getId() { return id; }
    public File getFile() { return file; }
    public abstract boolean canAccess(User user);
}