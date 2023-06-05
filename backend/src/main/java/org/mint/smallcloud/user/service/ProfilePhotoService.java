package org.mint.smallcloud.user.service;

import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.user.dto.PhotoDownloadResponseDto;

public interface ProfilePhotoService {
    PhotoDownloadResponseDto downloadPhoto(FileLocation location);
}
