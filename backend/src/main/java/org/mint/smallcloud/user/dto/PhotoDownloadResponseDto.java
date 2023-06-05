package org.mint.smallcloud.user.dto;

import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PhotoDownloadResponseDto {
    Resource photoResource;
    String contentType;
    Long contentLength;
}
