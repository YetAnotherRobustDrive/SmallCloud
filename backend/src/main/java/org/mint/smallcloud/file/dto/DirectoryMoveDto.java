package org.mint.smallcloud.file.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class DirectoryMoveDto {
    private final Long directoryId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DirectoryMoveDto(Long directoryId) {
        this.directoryId = directoryId;
    }

    public static DirectoryMoveDtoBuilder builder() {
        return new DirectoryMoveDtoBuilder();
    }

    public Long getDirectoryId() {
        return this.directoryId;
    }

    public static class DirectoryMoveDtoBuilder {
        private Long directoryId;

        DirectoryMoveDtoBuilder() {
        }

        public DirectoryMoveDtoBuilder directoryId(Long directoryId) {
            this.directoryId = directoryId;
            return this;
        }

        public DirectoryMoveDto build() {
            return new DirectoryMoveDto(this.directoryId);
        }

        public String toString() {
            return "DirectoryMoveDto.DirectoryMoveDtoBuilder(directoryId=" + this.directoryId + ")";
        }
    }
}
