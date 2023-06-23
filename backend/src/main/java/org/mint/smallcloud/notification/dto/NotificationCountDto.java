package org.mint.smallcloud.notification.dto;

import java.util.List;

public class NotificationCountDto {
    List<NotificationResponseDto> notificationDtoList;
    Long count;

    public NotificationCountDto(List<NotificationResponseDto> notificationDtoList, Long count) {
        this.notificationDtoList = notificationDtoList;
        this.count = count;
    }

    public static NotificationCountDtoBuilder builder() {
        return new NotificationCountDtoBuilder();
    }

    public List<NotificationResponseDto> getNotificationDtoList() {
        return this.notificationDtoList;
    }

    public Long getCount() {
        return this.count;
    }

    public static class NotificationCountDtoBuilder {
        private List<NotificationResponseDto> notificationDtoList;
        private Long count;

        NotificationCountDtoBuilder() {
        }

        public NotificationCountDtoBuilder notificationDtoList(List<NotificationResponseDto> notificationDtoList) {
            this.notificationDtoList = notificationDtoList;
            return this;
        }

        public NotificationCountDtoBuilder count(Long count) {
            this.count = count;
            return this;
        }

        public NotificationCountDto build() {
            return new NotificationCountDto(this.notificationDtoList, this.count);
        }

        public String toString() {
            return "NotificationCountDto.NotificationCountDtoBuilder(notificationDtoList=" + this.notificationDtoList + ", count=" + this.count + ")";
        }
    }
}
