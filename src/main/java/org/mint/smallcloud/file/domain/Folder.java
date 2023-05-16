package org.mint.smallcloud.file.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "FOLDERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Folder extends DataNode {

    @OneToMany(
        mappedBy = "parentFolder",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<DataNode> subDataNodes;

    protected Folder(String name, Member member) {
        super(FileType.of(name, FileType.FOLDER), member.getId());
        subDataNodes = null;
    }

    public static Folder of(String name, Member member) {
        return new Folder(name, member);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof Folder
            && ((Folder) obj).getId().equals(this.getId());
    }
}