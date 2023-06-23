package org.mint.smallcloud.log.dto;

import java.time.LocalDateTime;

public class ResponseLogDto {
    private final String nickName;
    private final LocalDateTime localDateTime;
    private final String action;
    private final String ipAddr;
    private final Boolean status;

    public ResponseLogDto(String nickName, LocalDateTime localDateTime, String action, String ipAddr, Boolean status) {
        this.nickName = nickName;
        this.localDateTime = localDateTime;
        this.action = action;
        this.ipAddr = ipAddr;
        this.status = status;
    }

    public static ResponseLogDtoBuilder builder() {
        return new ResponseLogDtoBuilder();
    }

    @Override
    public String toString() {
        return "UserLog{" +
            "nickName=" + nickName +
            ", action=" + action +
            ", time=" + localDateTime +
            ", ipAddr=" + ipAddr +
            ", status=" + status.toString() +
            '}';
    }

    public String getNickName() {
        return this.nickName;
    }

    public LocalDateTime getLocalDateTime() {
        return this.localDateTime;
    }

    public String getAction() {
        return this.action;
    }

    public String getIpAddr() {
        return this.ipAddr;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public static class ResponseLogDtoBuilder {
        private String nickName;
        private LocalDateTime localDateTime;
        private String action;
        private String ipAddr;
        private Boolean status;

        ResponseLogDtoBuilder() {
        }

        public ResponseLogDtoBuilder nickName(String nickName) {
            this.nickName = nickName;
            return this;
        }

        public ResponseLogDtoBuilder localDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
            return this;
        }

        public ResponseLogDtoBuilder action(String action) {
            this.action = action;
            return this;
        }

        public ResponseLogDtoBuilder ipAddr(String ipAddr) {
            this.ipAddr = ipAddr;
            return this;
        }

        public ResponseLogDtoBuilder status(Boolean status) {
            this.status = status;
            return this;
        }

        public ResponseLogDto build() {
            return new ResponseLogDto(this.nickName, this.localDateTime, this.action, this.ipAddr, this.status);
        }

        public String toString() {
            return "ResponseLogDto.ResponseLogDtoBuilder(nickName=" + this.nickName + ", localDateTime=" + this.localDateTime + ", action=" + this.action + ", ipAddr=" + this.ipAddr + ", status=" + this.status + ")";
        }
    }
}
