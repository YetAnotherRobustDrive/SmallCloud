package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.dto.DirectoryCreateDto;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

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
}
