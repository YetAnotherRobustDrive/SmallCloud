package org.mint.smallcloud.file.repository;

import org.mint.smallcloud.file.domain.Segment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegmentRepository extends CrudRepository<Segment, Long> {
    List<Segment> findByFileIdAndName(Long fileId, String name);
}
