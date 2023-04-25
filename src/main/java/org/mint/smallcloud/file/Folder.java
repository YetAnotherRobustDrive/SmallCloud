package org.mint.smallcloud.file;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "FOLDERS")
@NoArgsConstructor
public class Folder {

    @Id
    @Column(name = "FOLDER_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createdDate;


    @Column(name = "PARENTS_FOLDER")
    private Folder parentFolder;

    @OneToMany(mappedBy = "file")
    private List<File> filesublist;

    @OneToMany(mappedBy = "folder")
    private List<Folder> foldersublist;

    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public Folder getParentFolder() { return parentFolder; }
    public List<File> getFileSubList() { return filesublist; }
    public List<Folder> getFolderSubList() { return foldersublist; }

    public void setName(String name) { this.name = name; }
    public void setCreatedDate(LocalDateTime localDateTime) { this.createdDate = createdDate; }
    public void setParentFolder(Folder parentFolder) { this.parentFolder = parentFolder; }
}

// 폴더 트리 만들기위해 연관관계 생각해야댐 - parentFolder, foldersublist