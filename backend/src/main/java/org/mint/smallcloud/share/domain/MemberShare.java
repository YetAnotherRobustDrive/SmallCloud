package org.mint.smallcloud.share.domain;

import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "MEMBER_SHARES")
@Entity
public class MemberShare extends Share {
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Member target;

    protected MemberShare(Member target, DataNode file) {
        super(file);
        this.target = target;
    }

    protected MemberShare() {
    }

    public static MemberShare of(Member target, DataNode file) {
        MemberShare rst = new MemberShare(target, file);
        target.addShare(rst);
        file.addShare(rst);
        return rst;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof MemberShare
            && ((MemberShare) obj).getId().equals(this.getId());
    }

    @Override
    public boolean canAccess(Member member) {
        return canAccess(member.getUsername());
    }

    @Override
    public boolean canAccess(String username) {
        return this.getTarget().getUsername().equals(username);
    }

    @Override
    public String getTargetName() {
        return getTarget().getUsername();
    }

    public Member getTarget() {
        return this.target;
    }
}
