package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.file.dto.DirectoryCreateDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectoryFacadeService {
    public void create(Long directoryId, DirectoryCreateDto dto, String username) {
    }
}
