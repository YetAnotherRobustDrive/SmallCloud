package org.mint.smallcloud.share;

import org.mint.smallcloud.user.User;

public class UserShare extends Share {
    private User target;

    @Override
    public boolean canAccess(User user) {
        return false;
    }
}
