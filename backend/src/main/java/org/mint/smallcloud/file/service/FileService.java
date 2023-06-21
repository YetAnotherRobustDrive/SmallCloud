package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.dto.UsageDto;
import org.mint.smallcloud.file.repository.FileRepository;
import org.mint.smallcloud.label.service.LabelService;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FileService {
    private final LabelService labelService;
    private final FileRepository fileRepository;

    public void deleteFile(File file, Member user) {
        labelService.attachTrash(file, user);
    }
    public void restoreFile(File file, Member user) {
        labelService.detachTrash(file, user);
    }
    public void moveFile(File targetFile, Folder destFolder) {
        targetFile.setParentFolder(destFolder);
    }
    public void favoriteFile(File file, Member user) {
        labelService.attachFavorite(file, user);
    }
    public void unFavoriteFile(File file, Member user) {
        labelService.detachFavorite(file, user);
    }

    public List<File> search(String q, Member user) {
        if (q.isBlank())
            return List.<File>of();
        return fileRepository.findByFileType_NameLikeAndOwner(q, user);
    }


    public UsageDto getUsage(Member user) {
        return UsageDto.builder()
                .used(fileRepository.sumSizeByOwner(user))
                .build();
    }
}
