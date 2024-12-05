package cn.hjf.job.upload.service.impl;

import cn.hjf.job.common.minio.config.MinioProperties;
import cn.hjf.job.upload.service.FileUploadService;
import cn.hjf.job.upload.utils.FileNameUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file, String pathPrefix) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 获取文件名
        String originalFilename = file.getOriginalFilename();

        // 如果文件名为空，抛出异常或者返回默认值
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        // 获取文件扩展名
        String extFileName = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 文件路径和文件名
        String fileName = FileNameUtils.generatorFileName(pathPrefix) + extFileName;

        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(fileName)
                .build();

        minioClient.putObject(putObjectArgs);

        return minioProperties.getUrl() + "/" + minioProperties.getBucket() + "/" + fileName;
    }
}
