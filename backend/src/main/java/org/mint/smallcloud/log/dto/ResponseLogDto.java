package org.mint.smallcloud.log.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class ResponseLogDto {
    private final String nickName;
    private final LocalDateTime localDateTime;
    private final String action;
    private final String ipAddr;
    private final Boolean status;
}
