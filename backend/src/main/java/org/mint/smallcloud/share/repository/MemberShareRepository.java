package org.mint.smallcloud.share.repository;

import org.mint.smallcloud.share.domain.MemberShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberShareRepository extends JpaRepository<MemberShare, Long> {
    boolean existsByFileIdAndTargetName(Long fileId, String memberName);

    void deleteByFileIdAndTargetName(Long id, String targetName);
}
