package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.domain.IndexData;
import org.mint.smallcloud.file.repository.IndexDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SegmentService {
    private final IndexDataRepository indexDataRepository;

    public void createIndexData(File originFile, String location) {
        indexDataRepository.save(IndexData.of(originFile, location));
    }
}
