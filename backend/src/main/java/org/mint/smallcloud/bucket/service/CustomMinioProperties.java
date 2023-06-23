package org.mint.smallcloud.bucket.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class CustomMinioProperties {
    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public String getMinioUrl() {
        return this.minioUrl;
    }

    public String getBucketName() {
        return this.bucketName;
    }
}
