package org.mint.smallcloud.file.dto;


import lombok.Getter;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
public class FileDto extends DataNodeDto{
    private final Long size;
    private final String location;
}
