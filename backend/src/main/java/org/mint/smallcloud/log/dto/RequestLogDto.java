package org.mint.smallcloud.log.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class RequestLogDto {
    private final String userName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String action;
    private final Boolean status;
}
