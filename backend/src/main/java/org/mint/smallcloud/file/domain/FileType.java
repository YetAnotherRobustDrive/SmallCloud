package org.mint.smallcloud.file.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FileType {
    public static final String FOLDER = "FOLDER";

    @Column(name = "NAME")
    private String name;
    @Column(name = "TYPE")
    private String type;

    protected FileType(String name, String type) {
        this.name = name;
        this.type = type;
    }

    protected FileType() {
    }

    public static FileType of(String name, String type) {
        return new FileType(name, type);
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }
}
