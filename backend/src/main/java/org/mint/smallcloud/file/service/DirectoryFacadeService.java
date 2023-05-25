package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.dto.*;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectoryFacadeService {
    private final DirectoryThrowerService directoryThrowerService;
    private final DirectoryService directoryService;
    private final MemberThrowerService memberThrowerService;

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
        return DirectoryDto.builder().build();
    }

    public List<DirectoryDto> subDirectories(Long directoryId, String username) {
        return null;
    }

    public List<FileDto> files(Long directoryId, String username) {
        return null;
    }
}
