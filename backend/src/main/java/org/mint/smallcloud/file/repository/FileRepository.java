package org.mint.smallcloud.file.repository;

import org.mint.smallcloud.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileType_NameAndParentFolder_Id(String name, Long parentFolderId);

}
