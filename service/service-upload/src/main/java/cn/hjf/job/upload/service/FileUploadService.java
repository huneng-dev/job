package cn.hjf.job.upload.service;

import cn.hjf.job.model.dto.upload.UploadStatusDTO;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 文件上传服务
 *
 * @author hjf
 * @version 1.0
 * @description
 */

public interface FileUploadService {

    String upload(MultipartFile file, String pathPrefix) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    UploadStatusDTO initUpload(String fileHash,String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    UploadStatusDTO getUploadStatus(String hash);

    void uploadChunk(String hash, int index, int total, MultipartFile chunk);

    String mergeChunks(String hash);
}
