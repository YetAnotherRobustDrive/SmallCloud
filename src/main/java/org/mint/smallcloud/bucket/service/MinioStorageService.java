package org.mint.smallcloud.bucket.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.bucket.dto.FileObjectDto;
import org.mint.smallcloud.bucket.exception.DownloadException;
import org.mint.smallcloud.bucket.exception.RemoveException;
import org.mint.smallcloud.bucket.exception.StorageSettingException;
import org.mint.smallcloud.bucket.exception.UploadException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;
    private final CustomMinioProperties minioProperties;

    @Override
    public FileObjectDto uploadFile(InputStream stream, String contentType, long size) throws UploadException, StorageSettingException {
        String objectId = generateId();
        PutObjectArgs arg = PutObjectArgs.builder()
            .bucket(minioProperties.getBucketName())
            .object(objectId)
            .stream(stream, size, -1)
            .contentType(contentType)
            .build();
        try {
            minioClient.putObject(arg);
        } catch (InsufficientDataException e) {
            throw new UploadException();
        } catch (Exception e) {
            throw new StorageSettingException(e.getMessage());
        }
        return FileObjectDto.builder()
            .objectId(objectId)
            .contentType(contentType)
            .size(size).build();
    }

    @Override
    public void removeFile(String objectId) throws RemoveException, StorageSettingException {
        RemoveObjectArgs arg = RemoveObjectArgs.builder()
            .bucket(minioProperties.getBucketName())
            .object(objectId)
            .build();
        try {
            minioClient.removeObject(arg);
        } catch (ErrorResponseException e) {
            throw new RemoveException();
        } catch (Exception e) {
            throw new StorageSettingException(e.getMessage());
        }
    }

    @Override
    public InputStream downloadFile(String objectId) throws DownloadException, StorageSettingException {
        GetObjectArgs arg = GetObjectArgs.builder()
            .bucket(minioProperties.getBucketName())
            .object(objectId)
            .build();
        GetObjectResponse res = null;
        try {
            res = minioClient.getObject(arg);
        } catch (ErrorResponseException e) {
            throw new DownloadException();
        } catch (Exception e) {
            throw new StorageSettingException(e.getMessage());
        }
        return res;
    }

    @Override
    public boolean isFileExist(String objectId) throws StorageSettingException {
        StatObjectArgs statObjectArgs = StatObjectArgs.builder()
            .bucket(minioProperties.getBucketName()).object(objectId).build();
        try {
            minioClient.statObject(statObjectArgs);
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            throw new StorageSettingException(e.getMessage());
        }
    }

    private String generateId() throws StorageSettingException {
        String uuid = null;
        boolean exists = true;
        while (exists) {
            uuid = UUID.randomUUID().toString();
            exists = isFileExist(uuid);
        }
        return uuid;
    }
}
