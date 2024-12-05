package cn.hjf.job.common.minio.resolver;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 解析私有 minioUrl
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@Component
public class PrivateFileUrlResolver {
    @Resource
    private MinioClient minioClient;

    /**
     * @param path   文件路径
     * @param expiry 有效期，单位秒
     * @return 完整的私有文件 URL，带签名
     */
    public String resolveSingleUrl(String path, int expiry) {
        if (path == null || path.isEmpty()) {
            return "path:null";
        }

        // 去除开头的斜杠，以便正确分割
        String normalizedPath = path.startsWith("/") ? path.substring(1) : path;

        // 假设路径的第一个部分是存储桶名称，之后是文件路径
        String[] parts = normalizedPath.split("/", 2);
        if (parts.length < 2) {
            return "path:error";
        }

        String bucketName = parts[0];  // 存储桶名称
        String objectPath = parts[1];  // 文件路径

        if (bucketName == null || objectPath == null || bucketName.isEmpty() || objectPath.isEmpty()) {
            return "path:error";
        }

        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectPath)
                            .method(Method.GET)
                            .expiry(expiry, TimeUnit.SECONDS)
                            .build()
            );
        } catch (ServerException | NoSuchAlgorithmException | InternalException | XmlParserException |
                 InvalidResponseException | InvalidKeyException | IOException | ErrorResponseException |
                 InsufficientDataException e) {
            return "path:error";
        }


    }

    /**
     * 批量解析多个文件的私有 URL（带签名）
     * 性能较差慎用
     *
     * @param paths  文件路径列表
     * @param expiry 有效期，单位秒
     * @return 完整的私有文件 URL 列表，带签名
     */
    public List<String> resolveMultipleUrls(List<String> paths, int expiry) {
        // 使用普通的 stream 处理，保持顺序
        return paths.stream()
                .map(path -> {
                    try {
                        // 尝试获取签名的 URL
                        return resolveSingleUrl(path, expiry);
                    } catch (Exception e) {
                        // 如果出现异常，返回 null 或自定义的错误标识
                        return "path:null"; // 或者 "ERROR: " + path 作为错误占位符
                    }
                })
                .collect(Collectors.toList()); // 保持顺序，收集结果
    }
}
