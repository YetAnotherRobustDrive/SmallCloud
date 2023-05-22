package org.mint.smallcloud.file.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileLocation implements Serializable {
    @Column(name = "LOCATION", length = 40)
    private String location;

    protected FileLocation(String location) {
        this.location = location;
    }

    public static FileLocation of(String location) {
        return new FileLocation(location);
    }
}
