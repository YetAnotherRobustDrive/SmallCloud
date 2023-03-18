package org.mint.smallcloud.file;

import java.util.ArrayList;
import java.util.List;

public class FileService {
    public boolean isPathExist(List<String> folders) { return true; }
    public void moveFileToPath(List<String> folders) {}
    public void uploadFile(String file) {}
    public String downloadFile(Long id) { return ""; }
    public void removeFolder(Long id) {}
    public void makeFolder(String folder) {}
    public List<String> getFolderSortBy(Long id) { return new ArrayList<>(); }
    public String getFile(Long id) {return "";}

}
