package cn.hjf.job.upload.controller;

import cn.hjf.job.common.constant.UploadPathConstant;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.dto.company.BusinessLicenseDTO;
import cn.hjf.job.model.vo.company.BusinessLicenseVo;
import cn.hjf.job.model.vo.company.LegalPersonInfoVo;
import cn.hjf.job.upload.exception.BusinessLicenseException;
import cn.hjf.job.upload.service.FileUploadService;
import cn.hjf.job.upload.service.TXOcrService;
import cn.hjf.job.upload.utils.FileTypeValidatorUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

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
        List<String> IMAGE_MIME_TYPES = Arrays.asList("image/jpeg", "image/png");

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
        List<String> IMAGE_MIME_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

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

    /**
     * 营业执照上传
     *
     * @param file 营业执照图片
     * @return 回显 url 和 营业执照 Ocr 数据
     */
    @PostMapping("/company/business/license")
    public Result<BusinessLicenseDTO> businessLicense(@RequestPart("file") MultipartFile file) {
        // 设置类型校验
        List<String> IMAGE_MIME_TYPES = Arrays.asList("image/jpeg", "image/png");

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
            BusinessLicenseDTO businessLicenseDTO = new BusinessLicenseDTO(url, businessLicenseVo);
            return Result.ok(businessLicenseDTO);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 InvalidKeyException | NoSuchAlgorithmException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            return Result.fail();
        } catch (BusinessLicenseException | TencentCloudSDKException e) {
            return Result.build(null, 422, e.getMessage());
        }
    }

    /**
     * 上传身份证正面
     *
     * @param file 身份证图片
     * @return 身份证回显数据
     */
    @PostMapping("/front/idcard")
    public Result<LegalPersonInfoVo> frontIdCard(@RequestPart("file") MultipartFile file) {
        // 设置类型校验
        List<String> IMAGE_MIME_TYPES = Arrays.asList("image/jpeg", "image/png");

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
            String url = fileUploadService.upload(file, UploadPathConstant.FRONT_ID_CARD);

            // 调用腾讯云 SDK 识别营业执照
            byte[] fileBytes = file.getBytes();
            String imageBase64 = Base64.getEncoder().encodeToString(fileBytes);

            IDCardOCRResponse idCardOCRResponse = txOcrService.IDCardOCR(imageBase64, "FRONT");
            LegalPersonInfoVo legalPersonInfoVo = new LegalPersonInfoVo();
            legalPersonInfoVo.setName(idCardOCRResponse.getName());
            legalPersonInfoVo.setGender(Objects.equals("男", idCardOCRResponse.getSex()) ? 1 : 2);

            // 定义格式化器，解析 yyyy/M/d 格式的日期
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

            // 将字符串转换为 LocalDate
            LocalDate birthDate = LocalDate.parse(idCardOCRResponse.getBirth(), formatter);
            legalPersonInfoVo.setBirthday(birthDate);
            legalPersonInfoVo.setIdcardAddress(idCardOCRResponse.getAddress());
            legalPersonInfoVo.setIdcardNo(idCardOCRResponse.getIdNum());
            legalPersonInfoVo.setIdcardFrontUrl(url);
            return Result.ok(legalPersonInfoVo);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 InvalidKeyException | NoSuchAlgorithmException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            return Result.fail();
        } catch (BusinessLicenseException e) {
            return Result.build(null, 422, e.getMessage());
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传身份证反面
     *
     * @param file 身份证图片
     * @return Result<LegalPersonInfoVo>
     */
    @PostMapping("/back/idcard")
    public Result<LegalPersonInfoVo> backIdCard(@RequestPart("file") MultipartFile file) {
        // 设置类型校验
        List<String> IMAGE_MIME_TYPES = Arrays.asList("image/jpeg", "image/png");

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
            String url = fileUploadService.upload(file, UploadPathConstant.BACK_ID_CARD);

            // 调用腾讯云 SDK 识别营业执照
            byte[] fileBytes = file.getBytes();
            String imageBase64 = Base64.getEncoder().encodeToString(fileBytes);

            IDCardOCRResponse idCardOCRResponse = txOcrService.IDCardOCR(imageBase64, "BACK");
            LegalPersonInfoVo legalPersonInfoVo = new LegalPersonInfoVo();
            LocalDate expire = setIdcardExpireFromValidDate(idCardOCRResponse.getValidDate());

            legalPersonInfoVo.setIdcardExpire(expire);

            legalPersonInfoVo.setIdcardBackUrl(url);
            return Result.ok(legalPersonInfoVo);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 InvalidKeyException | NoSuchAlgorithmException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            return Result.fail();
        } catch (BusinessLicenseException e) {
            return Result.build(null, 422, e.getMessage());
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传公司照片
     *
     * @param file 公司图片
     * @return url
     */
    @PostMapping("/company/photo")
    public Result<String> companyPhoto(@RequestPart("file") MultipartFile file) {
        // 设置类型校验
        List<String> IMAGE_MIME_TYPES = Arrays.asList("image/jpeg", "image/png");

        // 设置文件大小限制，假设限制为 5MB
        long maxFileSize = 5 * 1024 * 1024;

        try {

            // 校验文件
            boolean validatorResult = FileTypeValidatorUtils.fileTypeValidator(file, IMAGE_MIME_TYPES);

            if (!validatorResult) {
                return Result.fail();
            }

            // 校验文件大小
            if (file.getSize() > maxFileSize) {
                return Result.fail("文件大小超过限制，最大为 5MB");
            }

            // 上传文件
            String url = fileUploadService.upload(file, UploadPathConstant.COMPANY_PHOTO);

            return Result.ok(url);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 InvalidKeyException | NoSuchAlgorithmException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            return Result.fail();
        }
    }

    /**
     * 上传聊天图片
     *
     * @param file 上传
     * @return 图片
     */
    @PostMapping("/chat/message/photo")
    public Result<String> chatMessagePhoto(
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) Long chatId
    ) {
        // 设置类型校验
        List<String> IMAGE_MIME_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

        // 设置文件大小限制，假设限制为 10MB
        long maxFileSize = 10 * 1024 * 1024;

        try {
            // 校验文件
            boolean validatorResult = FileTypeValidatorUtils.fileTypeValidator(file, IMAGE_MIME_TYPES);

            if (!validatorResult) {
                return Result.fail();
            }

            // 校验文件大小
            if (file.getSize() > maxFileSize) {
                return Result.fail("文件大小超过限制，最大为 10MB");
            }

            // 上传文件
            String url = fileUploadService.upload(file, UploadPathConstant.CHAT_MESSAGE_PHOTO + "/" + chatId);

            return Result.ok(url);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 InvalidKeyException | NoSuchAlgorithmException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            return Result.fail();
        }
    }

    /**
     * 上传聊天文件
     *
     * @param file 上传
     * @return 文件
     */
    @PostMapping("/chat/message/file")
    public Result<String> chatMessageFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) Long chatId
    ) {

        // 设置文件大小限制，假设限制为 10MB
        long maxFileSize = 10 * 1024 * 1024;

        try {
            // 校验文件
            boolean validatorResult = FileTypeValidatorUtils.validateFileType(file);

            if (!validatorResult) {
                return Result.fail();
            }

            // 校验文件大小
            if (file.getSize() > maxFileSize) {
                return Result.fail("文件大小超过限制，最大为 10MB");
            }

            // 上传文件
            String url = fileUploadService.upload(file, UploadPathConstant.CHAT_MESSAGE_FILE + "/" + chatId);

            return Result.ok(url);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 InvalidKeyException | NoSuchAlgorithmException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            return Result.fail();
        }
    }

    public LocalDate setIdcardExpireFromValidDate(String validDate) {
        // 按照 "-" 分割字符串，获取有效期的结束日期部分
        String expireDateStr = validDate.split("-")[1];

        // 使用 DateTimeFormatter 将字符串解析为 LocalDate 类型
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return LocalDate.parse(expireDateStr, formatter);
    }
}
