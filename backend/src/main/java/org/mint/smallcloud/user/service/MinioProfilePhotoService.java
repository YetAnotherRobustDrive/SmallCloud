package org.mint.smallcloud.user.service;

import io.minio.*;
import org.mint.smallcloud.bucket.service.CustomMinioProperties;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.user.dto.PhotoDownloadResponseDto;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class MinioProfilePhotoService implements ProfilePhotoService {
    private final MinioClient minioClient;
    private final CustomMinioProperties minioProperties;

    public MinioProfilePhotoService(MinioClient minioClient, CustomMinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }


    public PhotoDownloadResponseDto downloadPhoto(FileLocation location) {
        if (null == location) {
            throw new ServiceException(ExceptionStatus.FILE_NOT_FOUND);
        }
        String objId = location.getLocation();
        StatObjectArgs statObjectArgs = StatObjectArgs.builder()
            .bucket(minioProperties.getBucketName()).object(objId).build();
        StatObjectResponse statResponse;
        try {
            statResponse = minioClient.statObject(statObjectArgs);
        } catch (Exception e) {
            throw new ServiceException(ExceptionStatus.FILE_NOT_FOUND);
        }
        String contentType = statResponse.contentType();
        Long contentLength = statResponse.size();

        GetObjectArgs arg = GetObjectArgs.builder()
            .bucket(minioProperties.getBucketName())
            .object(location.getLocation())
            .build();
        GetObjectResponse res;
        try {
            res = minioClient.getObject(arg);
        } catch (Exception e) {
            throw new ServiceException(ExceptionStatus.FILE_NOT_FOUND);
        }
        return PhotoDownloadResponseDto.builder()
            .photoResource(new InputStreamResource(res))
            .contentType(contentType)
            .contentLength(contentLength)
            .build();
    }
}
