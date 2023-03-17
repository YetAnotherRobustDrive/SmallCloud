package org.mint.smallcloud.file;

import java.util.List;

public class Path {
    private final List<FolderVO> path;

    public Path(List<FolderVO> path) { this.path = path; }
    public String getFullPath(String sep) { return ""; }
}
