package org.mint.smallcloud.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder
@Getter
public class DirectoryMoveDto {
    private Long directoryId;
}
