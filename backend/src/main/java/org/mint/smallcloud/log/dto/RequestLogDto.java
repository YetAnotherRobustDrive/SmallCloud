package org.mint.smallcloud.log.dto;

import java.time.LocalDateTime;

public class RequestLogDto {
    private final String userName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String action;
    private final Boolean status;

    public RequestLogDto(String userName, LocalDateTime startTime, LocalDateTime endTime, String action, Boolean status) {
        this.userName = userName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.action = action;
        this.status = status;
    }

    public static RequestLogDtoBuilder builder() {
        return new RequestLogDtoBuilder();
    }

    public String getUserName() {
        return this.userName;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public String getAction() {
        return this.action;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public static class RequestLogDtoBuilder {
        private String userName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String action;
        private Boolean status;

        RequestLogDtoBuilder() {
        }

        public RequestLogDtoBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public RequestLogDtoBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public RequestLogDtoBuilder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public RequestLogDtoBuilder action(String action) {
            this.action = action;
            return this;
        }

        public RequestLogDtoBuilder status(Boolean status) {
            this.status = status;
            return this;
        }

        public RequestLogDto build() {
            return new RequestLogDto(this.userName, this.startTime, this.endTime, this.action, this.status);
        }

        public String toString() {
            return "RequestLogDto.RequestLogDtoBuilder(userName=" + this.userName + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", action=" + this.action + ", status=" + this.status + ")";
        }
    }
}
