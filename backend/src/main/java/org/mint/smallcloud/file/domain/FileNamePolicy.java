package org.mint.smallcloud.file.domain;

public interface FileNamePolicy {
    String nextFileName(String fileName);
}
