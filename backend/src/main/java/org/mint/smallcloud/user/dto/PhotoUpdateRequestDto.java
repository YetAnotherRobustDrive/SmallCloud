package org.mint.smallcloud.user.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PhotoUpdateRequestDto {
    final MultipartFile imageFile;
}
