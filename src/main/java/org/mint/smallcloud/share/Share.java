package org.mint.smallcloud.share;

import org.mint.smallcloud.file.File;
import org.mint.smallcloud.user.User;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Share {

    @Id
    @Column(name = "SHARE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FILE")
    private File file;

    public Long getId() {
        return id;
    }

    public File getFile() {
        return file;
    }

    public abstract boolean canAccess(User user);
}