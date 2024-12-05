package cn.hjf.job.common.minio.resolver;

import cn.hjf.job.common.minio.config.MinioProperties;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 解析公共 minioUrl
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@Component
public class PublicFileUrlResolver {

    @Resource
    private MinioProperties minioProperties;

    /**
     * 解析单个文件的公共 URL
     *
     * @param path 文件在 MinIO 存储服务中的路径
     * @return 完整的公共 URL
     */
    public String resolveSingleUrl(String path) {
        return minioProperties.getUrl() + path;
    }

    /**
     * 解析多个文件的公共 URL
     *
     * @param paths 文件在 MinIO 存储服务中的路径列表
     * @return 完整的公共 URL 列表
     */
    public List<String> resolveMultipleUrls(List<String> paths) {
        return paths.stream()
                .map(path -> minioProperties.getUrl() + path)
                .collect(Collectors.toList());
    }
}
