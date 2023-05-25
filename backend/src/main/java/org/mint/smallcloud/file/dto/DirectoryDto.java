package org.mint.smallcloud.file.dto;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder
@Getter
public class DirectoryDto {
    @JsonUnwrapped
    private DataNodeDto dataNodeDto;
}
