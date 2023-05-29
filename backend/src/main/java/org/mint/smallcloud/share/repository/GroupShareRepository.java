package org.mint.smallcloud.share.repository;

import org.mint.smallcloud.share.domain.GroupShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupShareRepository extends JpaRepository<GroupShare, Long> {
    boolean existsByFileIdAndTargetName(Long fileId, String groupName);

    void deleteByFileIdAndTargetName(Long id, String targetName);
}
