package org.mint.smallcloud.file.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;
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

    @Transient
    public static final String ROOT_NAME = "_ROOT_";

    public static Folder of(Folder folder, String name, Member member) {
        Folder ret = new Folder(name, member);
        ret.setParentFolder(folder);
        return ret;
    }

    public static Folder createRoot(Member member) {
        return new Folder(ROOT_NAME, member);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof Folder
            && ((Folder) obj).getId().equals(this.getId());
    }

    public void addChild(DataNode dataNode) {
        if (this.subDataNodes.contains(dataNode)) return;
        this.subDataNodes.add(dataNode);
        dataNode.setParentFolder(this);
    }

    public boolean hasChildWithName(String name) {
        for (DataNode dataNode : this.subDataNodes) {
            if (dataNode.getName().equals(name))
                return true;
        }
        return false;
    }
}