package org.mint.smallcloud.file.repository;

import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Optional<Folder> findByFileType_NameAndParentFolder_Id(String name, Long parentFolderId);

    Boolean existsByParentFolderAndFileType_Name(Folder parent, String name);

    Optional<Folder> findByParentFolderIsNullAndAuthor(Member author);

    @Query("select f from Folder f join f.labels l where l.name = :labelName and l.owner.nickname = :ownerName")
    List<Folder> findDataNodeByLabelNameAndOwner(String labelName, String ownerName);
}
