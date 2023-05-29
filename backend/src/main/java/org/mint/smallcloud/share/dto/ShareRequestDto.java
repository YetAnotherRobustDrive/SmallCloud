package org.mint.smallcloud.share.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ShareRequestDto {
    private final Long fileId;
    private final String targetName;
    private final ShareType type;
}
