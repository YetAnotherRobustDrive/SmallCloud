package org.mint.smallcloud.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FileType {

    @Column(name = "NAME")
    private String name;
    @Column(name = "TYPE")
    private String type;

    public String getType() { return type; }
    public String getName() { return name; }
}
