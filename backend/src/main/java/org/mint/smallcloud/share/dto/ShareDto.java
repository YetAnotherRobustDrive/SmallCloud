package org.mint.smallcloud.share.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ShareDto {
    private final Long id;
    private final Long fileId;
    private final String type;
    private final String targetName;
}
