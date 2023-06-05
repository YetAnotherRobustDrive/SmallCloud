package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.FileNamePolicy;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.repository.DataNodeRepository;
import org.mint.smallcloud.file.repository.FolderRepository;
import org.mint.smallcloud.label.service.LabelService;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DirectoryService {
    private final FolderRepository folderRepository;
    private final DataNodeRepository dataNodeRepository;
    private final LabelService labelService;
    private final FileNamePolicy fileNamePolicy;

    public void createRootDirectory(Member member) {
        folderRepository.save(Folder.createRoot(member));
    }

    public void createDirectory(Folder parent, String name, Member member) {
        if (folderRepository.existsByParentFolderAndFileType_Name(parent, name)) {
            createDirectory(parent, fileNamePolicy.nextFileName(name), member);
        } else {
            folderRepository.save(Folder.of(parent, name, member));
        }
    }

    public void renameDirectory(Folder folder, String name) {
        if (folderRepository.existsByParentFolderAndFileType_Name(folder.getParentFolder(), name)) {
            renameDirectory(folder, fileNamePolicy.nextFileName(name));
        } else {
            folder.setName(name);
        }
    }

    public void moveDirectory(Folder target, Folder dest) {
        if (folderRepository.existsByParentFolderAndFileType_Name(dest, target.getName())) {
            throw new ServiceException(ExceptionStatus.ALREADY_EXISTS_DIRECTORY);
        }
        target.setParentFolder(dest);
    }

    public void purgeDirectory(Folder folder) {
        folderRepository.deleteById(folder.getId());
    }

    public void deleteDirectory(Folder folder, Member user) {
        labelService.attachTrash(folder, user);
    }

    public void restoreDirectory(Folder folder, Member user) {
        labelService.attachTrash(folder, user);
    }
}
