package cn.hjf.job.upload.controller;

import cn.hjf.job.common.constant.UploadPathConstant;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.dto.company.BusinessLicenseDTO;
import cn.hjf.job.model.vo.company.BusinessLicenseVo;
import cn.hjf.job.upload.exception.BusinessLicenseException;
import cn.hjf.job.upload.service.FileUploadService;
import cn.hjf.job.upload.service.TXOcrService;
import cn.hjf.job.upload.utils.FileTypeValidatorUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
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
import java.util.Base64;
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

    @Resource
    private TXOcrService txOcrService;

    /**
     * 上传用户头像
     *
     * @param file 文件
     * @return 回显url
     */
    @PostMapping("/user/avatar")
    public Result<String> userAvatar(@RequestPart("file") MultipartFile file) {
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

    /**
     * 上传公司 logo
     *
     * @param file 文件
     * @return 回显 url
     */
    @PostMapping("/company/logo")
    public Result<String> companyLogo(@RequestPart("file") MultipartFile file) {
        // 设置类型校验
        List<String> IMAGE_MIME_TYPES = Arrays.asList(
                "image/jpeg", "image/png", "image/gif"
        );

        // 设置文件大小限制，假设限制为 5MB
        long maxFileSize = 3 * 1024 * 1024;  // 1MB

        try {


            // 校验文件
            boolean validatorResult = FileTypeValidatorUtils.fileTypeValidator(file, IMAGE_MIME_TYPES);

            if (!validatorResult) {
                return Result.fail();
            }

            // 校验文件大小
            if (file.getSize() > maxFileSize) {
                return Result.fail("文件大小超过限制，最大为 3MB");
            }

            // 上传文件
            String url = fileUploadService.upload(file, UploadPathConstant.COMPANY_LOGO);

            return Result.ok(url);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 InvalidKeyException | NoSuchAlgorithmException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            return Result.fail();
        }
    }


    @PostMapping("/company/business/license")
    public Result<BusinessLicenseDTO> businessLicense(@RequestPart("file") MultipartFile file) {
        // 设置类型校验
        List<String> IMAGE_MIME_TYPES = Arrays.asList(
                "image/jpeg", "image/png"
        );

        // 设置文件大小限制，假设限制为 5MB
        long maxFileSize = 5 * 1024 * 1024;  // 5MB

        try {

            // 校验文件
            boolean validatorResult = FileTypeValidatorUtils.fileTypeValidator(file, IMAGE_MIME_TYPES);

            if (!validatorResult) {
                return Result.fail();
            }

            // 校验文件大小
            if (file.getSize() > maxFileSize) {
                return Result.fail();
            }

            // 上传文件
            String url = fileUploadService.upload(file, UploadPathConstant.BUSINESS_LICENSE);

            // 调用腾讯云 SDK 识别营业执照
            byte[] fileBytes = file.getBytes();
            String imageBase64 = Base64.getEncoder().encodeToString(fileBytes);
            BusinessLicenseVo businessLicenseVo = txOcrService.BizLicenseOCR(imageBase64);
            BusinessLicenseDTO businessLicenseDTO = new BusinessLicenseDTO(
                    url,
                    businessLicenseVo
            );
            return Result.ok(businessLicenseDTO);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 InvalidKeyException | NoSuchAlgorithmException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            return Result.fail();
        } catch (BusinessLicenseException | TencentCloudSDKException e) {
            return Result.build(null, 422, e.getMessage());
        }
    }
}
