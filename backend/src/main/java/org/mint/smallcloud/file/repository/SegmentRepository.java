package org.mint.smallcloud.file.repository;

import java.util.List;

import org.mint.smallcloud.file.domain.Segment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SegmentRepository extends CrudRepository<Segment, Long> {
    List<Segment> findByFileIdAndName(Long fileId, String name);
}
