package org.mint.smallcloud.share.repository;

import org.mint.smallcloud.share.domain.GroupShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupShareRepository extends JpaRepository<GroupShare, Long> {
    boolean existsByFileIdAndTarget_Name(Long fileId, String groupName);

    void deleteByFileIdAndTarget_Name(Long id, String targetName);
}
