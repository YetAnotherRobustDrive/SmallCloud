package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.repository.FolderRepository;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectoryService {
    private final FolderRepository folderRepository;

    public void createRootDirectory(Member member) {
        folderRepository.save(Folder.createRoot(member));
    }

    public void createDirectory(Folder parent, String name, Member member) {
        folderRepository.save(Folder.of(parent, name, member));
    }
}
