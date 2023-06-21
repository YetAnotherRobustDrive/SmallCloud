package org.mint.smallcloud.file.repository;

import org.mint.smallcloud.file.domain.IndexData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<IndexData, Long> {
}
