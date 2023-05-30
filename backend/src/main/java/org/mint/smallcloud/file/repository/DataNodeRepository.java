package org.mint.smallcloud.file.repository;

import org.mint.smallcloud.file.domain.DataNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataNodeRepository extends JpaRepository<DataNode, Long> {
}
