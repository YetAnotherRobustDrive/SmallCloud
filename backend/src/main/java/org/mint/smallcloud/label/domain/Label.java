package org.mint.smallcloud.label.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "LABELS")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LABEL_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private Member owner;

    @ManyToMany
    @JoinTable(
        name = "LABEL_DATA_NODE",
        joinColumns = @JoinColumn(name = "LABEL_ID"),
        inverseJoinColumns = @JoinColumn(name = "DATA_NODE_ID")
    )
    private List<DataNode> files = new ArrayList<>();

    protected Label(String name, Member owner) {
        this.name = name;
        this.owner = owner;
    }

    public static Label of(String name, Member owner) {
        return new Label(name, owner);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof Label
            && ((Label) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public void addFile(DataNode file) {
        if (this.files.contains(file)) return;
        this.files.add(file);
        file.addLabel(this);
    }

    public void deleteFile(DataNode dataNode) {
        if (!this.files.contains(dataNode)) return;
        this.files.remove(dataNode);
        dataNode.deleteLabel(this);
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean isTrash() {
        return this.name.equals(DefaultLabelType.defaultTrash.getLabelName());
    }
}