package org.mint.smallcloud.file.repository;

import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataNodeRepository extends JpaRepository<DataNode, Long> {
    List<DataNode> findByAuthor(Member author);

    @Query("select dn from DataNode dn join dn.labels l where l.name = :labelName")
    List<DataNode> findDataNodeByLabelName(String labelName);
}
