package org.mint.smallcloud.bucket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FileObjectDto {
    private final String objectId;
    private final Long size;
    private final String contentType;
}
