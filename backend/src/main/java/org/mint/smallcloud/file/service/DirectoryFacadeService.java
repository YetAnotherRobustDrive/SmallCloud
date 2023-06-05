package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.dto.*;
import org.mint.smallcloud.file.mapper.FileMapper;
import org.mint.smallcloud.file.mapper.FolderMapper;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DirectoryFacadeService {
    private final DirectoryThrowerService directoryThrowerService;
    private final DirectoryService directoryService;
    private final MemberThrowerService memberThrowerService;
    private final FolderMapper folderMapper;
    private final FileMapper fileMapper;

    public void create(Long directoryId, DirectoryCreateDto dto, String username) {
        Folder folder = directoryThrowerService.getDirectoryById(directoryId);
        directoryThrowerService.checkAccessRight(folder, username);
        directoryService.createDirectory(
            folder, dto.getName(), memberThrowerService.getMemberByUsername(username));
    }

    public void rename(Long directoryId, DirectoryRenameDto dto, String username) {
        Folder folder = directoryThrowerService.getDirectoryById(directoryId);
        directoryThrowerService.checkAccessRight(folder, username);
        directoryService.renameDirectory(folder, dto.getName());
    }

    public void move(Long directoryId, DirectoryMoveDto dto, String username) {
        Folder target = directoryThrowerService.getDirectoryById(directoryId);
        Folder dest = directoryThrowerService.getDirectoryById(dto.getDirectoryId());
        directoryThrowerService.checkAccessRight(target, username);
        directoryThrowerService.checkAccessRight(dest, username);
        directoryService.moveDirectory(target, dest);
    }

    public DirectoryDto info(Long directoryId, String username) {
        Folder folder = directoryThrowerService.getDirectoryById(directoryId);
        directoryThrowerService.checkAccessRight(folder, username);
        return folderMapper.toDirectoryDto(folder);
    }

    public List<DirectoryDto> subDirectories(Long directoryId, String username) {
        Folder parentFolder = directoryThrowerService.getDirectoryById(directoryId);
        directoryThrowerService.checkAccessRight(parentFolder, username);
        List<Folder> subFolders = parentFolder.getSubFolders();
        return subFolders.stream().map(folderMapper::toDirectoryDto).collect(Collectors.toList());
    }

    public List<DirectoryDto> activeSubDirectories(Long directoryId, String username) {
        Folder parentFolder = directoryThrowerService.getDirectoryById(directoryId);
        if (!parentFolder.isActive()) throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        directoryThrowerService.checkAccessRight(parentFolder, username);
        List<Folder> subFolders = parentFolder.getSubFolders();
        return subFolders.stream().filter(Folder::isActive).map(folderMapper::toDirectoryDto).collect(Collectors.toList());
    }

    public List<FileDto> files(Long directoryId, String username) {
        Folder folder = directoryThrowerService.getDirectoryById(directoryId);
        directoryThrowerService.checkAccessRight(folder, username);
        List<File> files = folder.getFiles();
        return files.stream().map(fileMapper::toFileDto).collect(Collectors.toList());
    }

    public void purge(Long directoryId, String username) {
        Folder folder = directoryThrowerService.getDirectoryById(directoryId);
        if (folder.isRoot())
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        directoryThrowerService.checkAccessRight(folder, username);
        directoryService.purgeDirectory(folder);
    }

    public void delete(Long directoryId, String username) {
        Folder folder = directoryThrowerService.getDirectoryById(directoryId);
        Member user = memberThrowerService.getMemberByUsername(username);
        if (folder.isRoot())
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        directoryThrowerService.checkAccessRight(folder, username);
        directoryService.deleteDirectory(folder, user);
    }

    public void restore(Long directoryId, String username) {
        Folder folder = directoryThrowerService.getDirectoryById(directoryId);
        Member user = memberThrowerService.getMemberByUsername(username);
        directoryThrowerService.checkAccessRight(folder, username);
        directoryService.restoreDirectory(folder, user);
    }

    public List<FileDto> activeFiles(Long directoryId, String username) {
        Folder folder = directoryThrowerService.getDirectoryById(directoryId);
        if (!folder.isActive()) throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        directoryThrowerService.checkAccessRight(folder, username);
        List<File> files = folder.getFiles();
        return files.stream().filter(File::isActive).map(fileMapper::toFileDto).collect(Collectors.toList());
    }
}
