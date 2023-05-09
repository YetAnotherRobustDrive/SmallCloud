package org.mint.smallcloud.file.domain;

import java.util.List;

public class Path {
    private final List<Folder> path;

    public Path(List<Folder> path) {
        this.path = path;
    }

    public String getFullPath(String sep) {
        return "";
    }
}
