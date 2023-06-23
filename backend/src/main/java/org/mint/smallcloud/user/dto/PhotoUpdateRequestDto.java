package org.mint.smallcloud.user.dto;

import org.springframework.web.multipart.MultipartFile;

public class PhotoUpdateRequestDto {
    final MultipartFile imageFile;

    public PhotoUpdateRequestDto(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public static PhotoUpdateRequestDtoBuilder builder() {
        return new PhotoUpdateRequestDtoBuilder();
    }

    public MultipartFile getImageFile() {
        return this.imageFile;
    }

    public static class PhotoUpdateRequestDtoBuilder {
        private MultipartFile imageFile;

        PhotoUpdateRequestDtoBuilder() {
        }

        public PhotoUpdateRequestDtoBuilder imageFile(MultipartFile imageFile) {
            this.imageFile = imageFile;
            return this;
        }

        public PhotoUpdateRequestDto build() {
            return new PhotoUpdateRequestDto(this.imageFile);
        }

        public String toString() {
            return "PhotoUpdateRequestDto.PhotoUpdateRequestDtoBuilder(imageFile=" + this.imageFile + ")";
        }
    }
}
