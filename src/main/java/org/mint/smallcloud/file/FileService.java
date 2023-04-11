package org.mint.smallcloud.file;

import org.mint.smallcloud.file.Repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public boolean isPathExist(List<String> folders) { return true; }
    public void moveFileToPath(List<String> folders) {}
    public void uploadFile(String file) {}
    public String downloadFile(Long id) { return ""; }
    public void removeFolder(Long id) {}
    public void makeFolder(String folder) {}
    public List<String> getFolderSortBy(Long id) { return new ArrayList<>(); }
    public String getFile(Long id) {return "";}

}
