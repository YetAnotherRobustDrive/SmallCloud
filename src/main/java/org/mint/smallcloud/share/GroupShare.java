package org.mint.smallcloud.share;


import org.mint.smallcloud.group.Group;
import org.mint.smallcloud.user.User;

public class GroupShare extends Share {
    private Group target;

    @Override
    public boolean canAccess(User user) {
        return false;
    }
}
