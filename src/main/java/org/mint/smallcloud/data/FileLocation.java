package org.mint.smallcloud.data;

import java.io.Serializable;

public class FileLocation implements Serializable {
    private final String location;

    public FileLocation(String location) { this.location = location; }

    public String getLocation() { return location; }
}
