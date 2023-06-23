package org.mint.smallcloud.bucket.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.mint.smallcloud.bucket.service.CustomMinioProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class MinioConfiguration {
    private final CustomMinioProperties minioProperties;

    public MinioConfiguration(CustomMinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    @Bean
    public MinioClient minioClient(
        @Value("${minio.user}")
        String user,
        @Value("${minio.password}")
        String password
    ) throws Exception {
        MinioClient client = new MinioClient.Builder()
            .credentials(user, password)
            .endpoint(minioProperties.getMinioUrl())
            .build();
        try {
            boolean found;
            found = client.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucketName()).build());
            if (!found) {
                client.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return client;
    }


}
