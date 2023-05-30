package org.mint.smallcloud.share.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "GROUP_SHARES")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GroupShare extends Share {
    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group target;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof GroupShare
            && ((GroupShare) obj).getId().equals(this.getId());
    }

    protected GroupShare(Group target, DataNode file) {
        super(file);
        this.target = target;
    }

    public static GroupShare of(Group target, DataNode file) {
        GroupShare rst = new GroupShare(target, file);
        target.addShare(rst);
        file.addShare(rst);
        return rst;
    }

    @Override
    public boolean canAccess(Member member) {
        return canAccess(member.getUsername());
    }

    @Override
    public boolean canAccess(String username) {
        return this.getTarget().getMembers().stream()
            .anyMatch(member -> member.getUsername().equals(username));
    }

    @Override
    public String getTargetName() {
        return getTarget().getName();
    }
}
