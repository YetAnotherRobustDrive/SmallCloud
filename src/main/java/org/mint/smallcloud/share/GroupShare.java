package org.mint.smallcloud.share;


import org.mint.smallcloud.group.Group;
import org.mint.smallcloud.user.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GroupShare extends Share {
    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group target;

    @Override
    public boolean canAccess(User user) {
        return false;
    }
}
