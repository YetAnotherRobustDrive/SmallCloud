package org.mint.smallcloud.file.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class FileLocation implements Serializable {
    @Column(name = "LOCATION", length = 40)
    private String location;

    protected FileLocation(String location) {
        this.location = location;
    }

    protected FileLocation() {
    }

    public static FileLocation of(String location) {
        return new FileLocation(location);
    }

    public String getLocation() {
        return this.location;
    }
}
