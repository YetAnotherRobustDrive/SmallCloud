package org.mint.smallcloud.share.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;


@Table(name = "SHARES")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Share {

    @Id
    @Column(name = "SHARE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FILE")
    private DataNode file;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof Share
            && ((Share) obj).getId().equals(this.getId());
    }

    protected Share(File file) {
        this.file = file;
    }

    public static Share of(Member target, File file) {
        return MemberShare.of(target, file);
    }

    public static Share of(Group target, File file) {
        return GroupShare.of(target, file);
    }

    public void setFile(DataNode file) {
        if (file == null) {
            removeFile();
            return;
        }
        if (this.file != null && this.file.equals(file)) return;
        this.file = file;
        this.file.addShare(this);
    }

    public void removeFile() {
        if (this.file == null) return;
        this.file.deleteShare(this);
        this.file = null;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public abstract boolean canAccess(Member member);
}