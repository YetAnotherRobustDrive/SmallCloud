package org.mint.smallcloud.file.repository;

import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataNodeRepository extends JpaRepository<DataNode, Long> {
    List<DataNode> findByAuthor(Member author);
    List<DataNode> findByLabels(String labelName);
}
