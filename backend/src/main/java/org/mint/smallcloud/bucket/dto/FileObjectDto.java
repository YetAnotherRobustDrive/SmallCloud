package org.mint.smallcloud.bucket.dto;

public class FileObjectDto {
    private final String objectId;
    private final Long size;
    private final String contentType;

    public FileObjectDto(String objectId, Long size, String contentType) {
        this.objectId = objectId;
        this.size = size;
        this.contentType = contentType;
    }

    public static FileObjectDtoBuilder builder() {
        return new FileObjectDtoBuilder();
    }

    public String getObjectId() {
        return this.objectId;
    }

    public Long getSize() {
        return this.size;
    }

    public String getContentType() {
        return this.contentType;
    }

    public static class FileObjectDtoBuilder {
        private String objectId;
        private Long size;
        private String contentType;

        FileObjectDtoBuilder() {
        }

        public FileObjectDtoBuilder objectId(String objectId) {
            this.objectId = objectId;
            return this;
        }

        public FileObjectDtoBuilder size(Long size) {
            this.size = size;
            return this;
        }

        public FileObjectDtoBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public FileObjectDto build() {
            return new FileObjectDto(this.objectId, this.size, this.contentType);
        }

        public String toString() {
            return "FileObjectDto.FileObjectDtoBuilder(objectId=" + this.objectId + ", size=" + this.size + ", contentType=" + this.contentType + ")";
        }
    }
}
