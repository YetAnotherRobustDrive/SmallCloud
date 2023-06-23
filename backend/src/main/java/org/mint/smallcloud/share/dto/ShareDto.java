package org.mint.smallcloud.share.dto;

public class ShareDto {
    private final Long id;
    private final Long fileId;
    private final String type;
    private final String targetName;

    public ShareDto(Long id, Long fileId, String type, String targetName) {
        this.id = id;
        this.fileId = fileId;
        this.type = type;
        this.targetName = targetName;
    }

    public static ShareDtoBuilder builder() {
        return new ShareDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Long getFileId() {
        return this.fileId;
    }

    public String getType() {
        return this.type;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public static class ShareDtoBuilder {
        private Long id;
        private Long fileId;
        private String type;
        private String targetName;

        ShareDtoBuilder() {
        }

        public ShareDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ShareDtoBuilder fileId(Long fileId) {
            this.fileId = fileId;
            return this;
        }

        public ShareDtoBuilder type(String type) {
            this.type = type;
            return this;
        }

        public ShareDtoBuilder targetName(String targetName) {
            this.targetName = targetName;
            return this;
        }

        public ShareDto build() {
            return new ShareDto(this.id, this.fileId, this.type, this.targetName);
        }

        public String toString() {
            return "ShareDto.ShareDtoBuilder(id=" + this.id + ", fileId=" + this.fileId + ", type=" + this.type + ", targetName=" + this.targetName + ")";
        }
    }
}
