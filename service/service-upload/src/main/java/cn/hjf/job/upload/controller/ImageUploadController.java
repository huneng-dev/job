package cn.hjf.job.upload.controller;

import cn.hjf.job.common.constant.UploadPathConstant;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.upload.service.FileUploadService;
import cn.hjf.job.upload.utils.FileTypeValidatorUtils;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * 上传图片控制器
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController
@RequestMapping("/upload/image")
public class ImageUploadController {

    @Resource(name = "fileUploadServiceImpl")
    private FileUploadService fileUploadService;

    /**
     * 上传用户头像
     *
     * @param file 文件
     * @return 回显url
     */
    @PostMapping("/user/avatar")
    public Result<String> UserAvatar(@RequestPart("file") MultipartFile file) {
        // 设置类型校验
        List<String> IMAGE_MIME_TYPES = Arrays.asList(
                "image/jpeg", "image/png"
        );

        try {
            // 校验文件
            boolean validatorResult = FileTypeValidatorUtils.fileTypeValidator(file, IMAGE_MIME_TYPES);
            if (!validatorResult) {
                return Result.fail();
            }

            // 上传文件
            String url = fileUploadService.upload(file, UploadPathConstant.USER_AVATAR);

            return Result.ok(url);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 InvalidKeyException | NoSuchAlgorithmException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            return Result.fail();
        }
    }
}
