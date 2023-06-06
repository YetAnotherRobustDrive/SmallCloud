package org.mint.smallcloud.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class NotificationCountDto {
    List<NotificationDto> notificationDtoList;
    Long count;
}
