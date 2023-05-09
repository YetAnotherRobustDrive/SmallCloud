package org.mint.smallcloud.file.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FileType {

    @Column(name = "NAME")
    private String name;
    @Column(name = "TYPE")
    private String type;

    protected FileType(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public static FileType of(String name, String type) {
        return new FileType(name, type);
    }
}
