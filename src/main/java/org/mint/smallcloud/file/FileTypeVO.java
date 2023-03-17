package org.mint.smallcloud.file;

public class FileTypeVO {
    private final String name;
    private final String type;

    public FileTypeVO(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getType() { return type; }
    public String getName() { return name; }
}
