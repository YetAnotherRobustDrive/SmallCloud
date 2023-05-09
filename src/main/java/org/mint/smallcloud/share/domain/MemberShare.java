package org.mint.smallcloud.share.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "MEMBER_SHARES")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberShare extends Share {
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Member target;

    protected MemberShare(Member target, File file) {
        super(file);
        this.target = target;
    }

    public static MemberShare of(Member target, File file) {
        return new MemberShare(target, file);
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
        return false;
    }
}
