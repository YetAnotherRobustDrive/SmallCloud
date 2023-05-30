package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.repository.FolderRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectoryThrowerService {
    private final FolderRepository folderRepository;

    public Folder getDirectoryById(Long directoryId) {
        return folderRepository.findById(directoryId)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_DIRECTORY));
    }

    public void checkExistsByName(Folder parent, String name) {
        if (parent.hasChildWithName(name)) {
            throw new ServiceException(ExceptionStatus.ALREADY_EXISTS_DIRECTORY);
        }
    }

    public void checkAccessRight(Folder folder, String username) {
        if (!folder.canAccessUser(username)) {
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        }
    }
}
