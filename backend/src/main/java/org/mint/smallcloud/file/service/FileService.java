package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.repository.FileRepository;
import org.mint.smallcloud.label.service.LabelService;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FileService {
    private final LabelService labelService;
    private final FileRepository fileRepository;

//    @Autowired
//    public FileService(FileRepository fileRepository) {
//        this.fileRepository = fileRepository;
//    }

    public boolean isPathExist(List<String> folders) {
        return true;
    }

    public void moveFileToPath(List<String> folders) {
    }

    public void uploadFile(String file) {
    }

    public String downloadFile(Long id) {
        return "";
    }

    public void removeFolder(Long id) {
    }

    public void makeFolder(String folder) {
    }

    public List<String> getFolderSortBy(Long id) {
        return new ArrayList<>();
    }

    public String getFile(Long id) {
        return "";
    }

    public void deleteFile(File file, Member user) {
        labelService.attachTrash(file, user);
    }
    public void restoreFile(File file, Member user) {
        labelService.detachTrash(file, user);
    }

}
