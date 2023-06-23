package org.mint.smallcloud.file.service;

import org.mint.smallcloud.bucket.service.StorageService;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.dto.DirectoryMoveDto;
import org.mint.smallcloud.file.dto.FileDto;
import org.mint.smallcloud.file.dto.UsageDto;
import org.mint.smallcloud.file.mapper.FileMapper;
import org.mint.smallcloud.file.repository.FileRepository;
import org.mint.smallcloud.file.repository.IndexDataRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileFacadeService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileFacadeService.class);
    private final DirectoryThrowerService directoryThrowerService;
    private final FileThrowerService fileThrowerService;
    private final MemberThrowerService memberThrowerService;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final StorageService storageService;
    private final IndexDataRepository indexDataRepository;

    public FileFacadeService(DirectoryThrowerService directoryThrowerService, FileThrowerService fileThrowerService, MemberThrowerService memberThrowerService, FileService fileService, FileRepository fileRepository, FileMapper fileMapper, StorageService storageService, IndexDataRepository indexDataRepository) {
        this.directoryThrowerService = directoryThrowerService;
        this.fileThrowerService = fileThrowerService;
        this.memberThrowerService = memberThrowerService;
        this.fileService = fileService;
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.storageService = storageService;
        this.indexDataRepository = indexDataRepository;
    }

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

    public void purge(Long fileId, String username) throws Exception {
        File targetFile = fileThrowerService.getFileById(fileId);
        if (targetFile.getAuthor().getUsername().equals(username)) {
            targetFile.getLabels().clear();
            FileLocation loc = targetFile.getLocation();
            storageService.removeFile(loc.getLocation());
            if (targetFile.getIndexData() != null) {
                storageService.removeFile(targetFile.getIndexData().getLocation());
                indexDataRepository.delete(targetFile.getIndexData());
            }
            fileRepository.delete(targetFile);
        } else
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

    public List<FileDto> search(String q, String username) {
        Member user = memberThrowerService.getMemberByUsername(username);
        return fileService.search(q, user).stream().map(fileMapper::toFileDto).collect(Collectors.toList());
    }

    public UsageDto getUsage(String username) {
        Member user = memberThrowerService.getMemberByUsername(username);
        return fileService.getUsage(user);
    }

    public boolean isEncoded(Long fileId, String username) {
        Member user = memberThrowerService.getMemberByUsername(username);
        File file = fileThrowerService.getFileById(fileId);
        if (!file.canAccessUser(user))
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        return file.isEncoded();
    }
}
