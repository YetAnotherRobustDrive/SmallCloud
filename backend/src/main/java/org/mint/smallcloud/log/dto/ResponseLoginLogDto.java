package org.mint.smallcloud.log.dto;

import java.time.LocalDateTime;

public class ResponseLoginLogDto {
    private final LocalDateTime localDateTime;
    private final String action;
    private final String ipAddr;
    private final Boolean status;

    public ResponseLoginLogDto(LocalDateTime localDateTime, String action, String ipAddr, Boolean status) {
        this.localDateTime = localDateTime;
        this.action = action;
        this.ipAddr = ipAddr;
        this.status = status;
    }

    public static ResponseLoginLogDtoBuilder builder() {
        return new ResponseLoginLogDtoBuilder();
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

    public static class ResponseLoginLogDtoBuilder {
        private LocalDateTime localDateTime;
        private String action;
        private String ipAddr;
        private Boolean status;

        ResponseLoginLogDtoBuilder() {
        }

        public ResponseLoginLogDtoBuilder localDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
            return this;
        }

        public ResponseLoginLogDtoBuilder action(String action) {
            this.action = action;
            return this;
        }

        public ResponseLoginLogDtoBuilder ipAddr(String ipAddr) {
            this.ipAddr = ipAddr;
            return this;
        }

        public ResponseLoginLogDtoBuilder status(Boolean status) {
            this.status = status;
            return this;
        }

        public ResponseLoginLogDto build() {
            return new ResponseLoginLogDto(this.localDateTime, this.action, this.ipAddr, this.status);
        }

        public String toString() {
            return "ResponseLoginLogDto.ResponseLoginLogDtoBuilder(localDateTime=" + this.localDateTime + ", action=" + this.action + ", ipAddr=" + this.ipAddr + ", status=" + this.status + ")";
        }
    }
}
