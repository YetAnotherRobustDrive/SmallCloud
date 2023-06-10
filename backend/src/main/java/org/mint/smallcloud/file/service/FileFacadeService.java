package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.dto.DirectoryMoveDto;
import org.mint.smallcloud.file.repository.FileRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FileFacadeService {
    private final DirectoryThrowerService directoryThrowerService;
    private final FileThrowerService fileThrowerService;
    private final MemberThrowerService memberThrowerService;
    private final FileService fileService;
    private final FileRepository fileRepository;

    public void delete(Long fileId, String username) {
        File file = fileThrowerService.getFileById(fileId);
        Member user = memberThrowerService.getMemberByUsername(username);
        fileThrowerService.checkAccessRight(file, username);
        fileService.deleteFile(file, user);
    }

    public void restore(Long fileId, String username) {
        File file = fileThrowerService.getFileById(fileId);
        Member user = memberThrowerService.getMemberByUsername(username);
        fileThrowerService.checkAccessRight(file, username);
        fileService.restoreFile(file, user);
    }

    public void move(Long fileId, DirectoryMoveDto directoryMoveDto, String username) {
        File targetFile = fileThrowerService.getFileById(fileId);
        Folder destFolder = directoryThrowerService.getDirectoryById(directoryMoveDto.getDirectoryId());
        fileThrowerService.checkAccessRight(targetFile, username);
        directoryThrowerService.checkAccessRight(destFolder, username);
        fileService.moveFile(targetFile, destFolder);
    }

    public void purge(Long fileId, String username) {
        File targetFile = fileThrowerService.getFileById(fileId);
        if (targetFile.canAccessUser(username))
            fileRepository.delete(targetFile);
        else
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
    }

    public void favorite(Long fileId, String username) {
        File targetFile = fileThrowerService.getFileById(fileId);
        Member user = memberThrowerService.getMemberByUsername(username);
        fileThrowerService.checkAccessRight(targetFile, username);
        fileService.favoriteFile(targetFile, user);
    }

    public void unFavorite(Long fileId, String username) {
        File targetFile = fileThrowerService.getFileById(fileId);
        Member user = memberThrowerService.getMemberByUsername(username);
        fileThrowerService.checkAccessRight(targetFile, username);
        fileService.unFavoriteFile(targetFile, user);
    }

    public List<File> search(String q, String username) {
        Member user = memberThrowerService.getMemberByUsername(username);
        return fileService.search(q, user);
    }
}
