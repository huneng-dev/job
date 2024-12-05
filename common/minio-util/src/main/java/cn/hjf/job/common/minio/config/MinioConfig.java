package cn.hjf.job.common.minio.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MinioConfig {

    @Resource
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        // 通过 Nginx 代理访问 MinIO 集群
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();

        // 检查桶是否存在，如果不存在，创建它
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucket()).build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucket()).build());
        }

        return minioClient;
    }
}
