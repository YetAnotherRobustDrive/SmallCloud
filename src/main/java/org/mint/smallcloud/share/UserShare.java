package org.mint.smallcloud.share;

import org.mint.smallcloud.user.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserShare extends Share {
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User target;

    @Override
    public boolean canAccess(User user) {
        return false;
    }
}
