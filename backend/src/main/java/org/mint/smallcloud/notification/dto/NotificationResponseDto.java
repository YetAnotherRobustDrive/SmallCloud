package org.mint.smallcloud.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class NotificationResponseDto {
    Long id;
    String content;
    LocalDateTime localDateTime;
}
