package org.mint.smallcloud.file.service;

import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.repository.FileRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FileThrowerService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileThrowerService.class);
    private final FileRepository fileRepository;

    public FileThrowerService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public File getFileById(Long id) {
        return fileRepository.findById(id)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.FILE_NOT_FOUND));
    }

    public void checkAccessRight(File file, String username) {
        if (!file.getAuthor().getUsername().equals(username)) {
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        }
    }
}
