package org.mint.smallcloud.log.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class LogSizeDto{
    private final Long size;
    private final List<ResponseLogDto> responseLogDtoList;
}
