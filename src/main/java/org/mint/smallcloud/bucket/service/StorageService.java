package org.mint.smallcloud.bucket.service;

import org.mint.smallcloud.bucket.dto.FileObjectDto;
import org.mint.smallcloud.bucket.exception.DownloadException;
import org.mint.smallcloud.bucket.exception.RemoveException;
import org.mint.smallcloud.bucket.exception.StorageSettingException;
import org.mint.smallcloud.bucket.exception.UploadException;

import java.io.InputStream;

public interface StorageService {
    FileObjectDto uploadFile(InputStream stream, String contentType, long size) throws UploadException, StorageSettingException;

    void removeFile(String objectId) throws RemoveException, StorageSettingException;

    InputStream downloadFile(String objectId) throws DownloadException, StorageSettingException;

    boolean isFileExist(String objectId) throws StorageSettingException;
}
