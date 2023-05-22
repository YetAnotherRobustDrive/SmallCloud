package org.mint.smallcloud.bucket.service;

import org.mint.smallcloud.bucket.dto.FileObjectDto;
import org.mint.smallcloud.bucket.exception.DownloadException;
import org.mint.smallcloud.bucket.exception.RemoveException;
import org.mint.smallcloud.bucket.exception.StorageSettingException;
import org.mint.smallcloud.bucket.exception.UploadException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
class StorageServiceMock implements StorageService {

    @Override
    public FileObjectDto uploadFile(InputStream stream, String contentType, long size) throws UploadException, StorageSettingException {
        return FileObjectDto.builder()
            .objectId("abc")
            .contentType("def")
            .size(1L)
            .build();
    }

    @Override
    public void removeFile(String objectId) throws RemoveException, StorageSettingException {
    }

    @Override
    public InputStream downloadFile(String objectId) throws DownloadException, StorageSettingException {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
    }

    @Override
    public boolean isFileExist(String objectId) throws StorageSettingException {
        return true;
    }
}