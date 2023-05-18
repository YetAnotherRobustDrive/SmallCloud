package org.mint.smallcloud.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class DirectoryCreateDto {
    private String name;

}
