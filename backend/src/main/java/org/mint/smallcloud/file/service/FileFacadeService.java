package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FileFacadeService {
    private final FileThrowerService fileThrowerService;
    private final MemberThrowerService memberThrowerService;
    private final FileService fileService;

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
}
