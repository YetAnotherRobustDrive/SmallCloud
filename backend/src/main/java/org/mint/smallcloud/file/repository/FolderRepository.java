package org.mint.smallcloud.file.repository;

import org.mint.smallcloud.file.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Optional<Folder> findByFileType_NameAndParentFolder_Id(String name, Long parentFolderId);

    Boolean existsByParentFolderAndFileType_Name(Folder parent, String name);

}
