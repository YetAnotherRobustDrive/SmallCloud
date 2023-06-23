package org.mint.smallcloud.share.dto;

public class ShareRequestDto {
    private final Long fileId;
    private final String targetName;
    private final ShareType type;

    public ShareRequestDto(Long fileId, String targetName, ShareType type) {
        this.fileId = fileId;
        this.targetName = targetName;
        this.type = type;
    }

    public static ShareRequestDtoBuilder builder() {
        return new ShareRequestDtoBuilder();
    }

    public Long getFileId() {
        return this.fileId;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public ShareType getType() {
        return this.type;
    }

    public static class ShareRequestDtoBuilder {
        private Long fileId;
        private String targetName;
        private ShareType type;

        ShareRequestDtoBuilder() {
        }

        public ShareRequestDtoBuilder fileId(Long fileId) {
            this.fileId = fileId;
            return this;
        }

        public ShareRequestDtoBuilder targetName(String targetName) {
            this.targetName = targetName;
            return this;
        }

        public ShareRequestDtoBuilder type(ShareType type) {
            this.type = type;
            return this;
        }

        public ShareRequestDto build() {
            return new ShareRequestDto(this.fileId, this.targetName, this.type);
        }

        public String toString() {
            return "ShareRequestDto.ShareRequestDtoBuilder(fileId=" + this.fileId + ", targetName=" + this.targetName + ", type=" + this.type + ")";
        }
    }
}
