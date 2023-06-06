package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.repository.FileRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileThrowerService {
    private final FileRepository fileRepository;

    public File getFileById(Long id) {
        return fileRepository.findById(id)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.FILE_NOT_FOUND));
    }

    public void checkAccessRight(File file, String username) {
        if (!file.canAccessUser(username)) {
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        }
    }
}
