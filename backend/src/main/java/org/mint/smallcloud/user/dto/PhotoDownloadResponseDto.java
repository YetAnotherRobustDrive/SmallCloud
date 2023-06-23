package org.mint.smallcloud.user.dto;

import org.springframework.core.io.Resource;

public class PhotoDownloadResponseDto {
    Resource photoResource;
    String contentType;
    Long contentLength;

    public PhotoDownloadResponseDto(Resource photoResource, String contentType, Long contentLength) {
        this.photoResource = photoResource;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public static PhotoDownloadResponseDtoBuilder builder() {
        return new PhotoDownloadResponseDtoBuilder();
    }

    public Resource getPhotoResource() {
        return this.photoResource;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Long getContentLength() {
        return this.contentLength;
    }

    public static class PhotoDownloadResponseDtoBuilder {
        private Resource photoResource;
        private String contentType;
        private Long contentLength;

        PhotoDownloadResponseDtoBuilder() {
        }

        public PhotoDownloadResponseDtoBuilder photoResource(Resource photoResource) {
            this.photoResource = photoResource;
            return this;
        }

        public PhotoDownloadResponseDtoBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public PhotoDownloadResponseDtoBuilder contentLength(Long contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public PhotoDownloadResponseDto build() {
            return new PhotoDownloadResponseDto(this.photoResource, this.contentType, this.contentLength);
        }

        public String toString() {
            return "PhotoDownloadResponseDto.PhotoDownloadResponseDtoBuilder(photoResource=" + this.photoResource + ", contentType=" + this.contentType + ", contentLength=" + this.contentLength + ")";
        }
    }
}
