package org.mint.smallcloud.bucket.service;

import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.user.dto.PhotoDownloadResponseDto;
import org.mint.smallcloud.user.service.ProfilePhotoService;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceMock implements ProfilePhotoService {

    @Override
    public PhotoDownloadResponseDto downloadPhoto(FileLocation location) {
        // TODO Auto-generated method stub
        return null;
    }
    
}

