package org.mint.smallcloud.share;

import org.mint.smallcloud.file.File;
import org.mint.smallcloud.user.User;

public abstract class Share {
    private Long id;
    private File file;

    public Long getId() { return id; }
    public File getFile() { return file; }
    public abstract boolean canAccess(User user);
}
