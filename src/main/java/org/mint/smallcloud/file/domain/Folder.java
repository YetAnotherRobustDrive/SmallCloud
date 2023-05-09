package org.mint.smallcloud.file.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "FOLDERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOLDER_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "PARENT_FOLDER_ID")
    private Folder parentFolder;

    @OneToMany(
        mappedBy = "parentFolder",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Folder> subFolders;

    @OneToMany(mappedBy = "folder")
    private List<File> subFiles;

    protected Folder(String name, Folder parentFolder) {
        this.name = name;
        this.parentFolder = parentFolder;
        this.createdDate = LocalDateTime.now();
    }

    public static Folder of(String name, Folder parentFolder) {
        return new Folder(name, parentFolder);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof Folder
            && ((Folder) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}