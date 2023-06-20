package org.mint.smallcloud.file.repository;

import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileType_NameAndParentFolder_Id(String name, Long parentFolderId);

    @Query("select f from File f join f.labels l where l.name = :labelName and l.owner.username = :ownerName")
    List<File> findDataNodeByLabelNameAndOwner(String labelName, String ownerName);

    @Query("select f from File f where f.fileType.name like %:q% and f.author = :member")
    List<File> findByFileType_NameLikeAndOwner(String q, Member member);

    @Query("select sum(f.size) from File f where f.author = :user")
    Long sumSizeByOwner(Member user);

}
