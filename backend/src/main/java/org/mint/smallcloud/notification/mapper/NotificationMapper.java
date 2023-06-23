package org.mint.smallcloud.notification.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.notification.domain.Notification;
import org.mint.smallcloud.notification.dto.NotificationDto;
import org.mint.smallcloud.notification.dto.NotificationResponseDto;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    NotificationDto toNotificationDto(Notification notification);

    NotificationResponseDto toNotificationResponseDto(Notification notification);
}
