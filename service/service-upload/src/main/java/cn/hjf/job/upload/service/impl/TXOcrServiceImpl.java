package cn.hjf.job.upload.service.impl;

import cn.hjf.job.model.vo.company.BusinessLicenseVo;
import cn.hjf.job.upload.config.TencentCloudProperties;
import cn.hjf.job.upload.exception.BusinessLicenseException;
import cn.hjf.job.upload.service.TXOcrService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.*;

/**
 * @author hjf
 * @version 1.0
 * @description
 */
@Service
public class TXOcrServiceImpl implements TXOcrService {

    @Resource
    private TencentCloudProperties tencentCloudProperties;

    @Override
    public BusinessLicenseVo BizLicenseOCR(String imageBase64) throws TencentCloudSDKException {
        // 构建Credential，验证秘钥
        Credential cred = new Credential(tencentCloudProperties.getSecretId(), tencentCloudProperties.getSecretKey());

        // 设置HTTP请求配置
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ocr.tencentcloudapi.com");

        // 设置Client配置
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        // 创建OCR客户端
        OcrClient client = new OcrClient(cred, "", clientProfile);

        // 构造请求对象
        BizLicenseOCRRequest req = new BizLicenseOCRRequest();
        req.setImageBase64(imageBase64);


        // 执行OCR请求
        BizLicenseOCRResponse resp = client.BizLicenseOCR(req);

        // 检查OCR识别结果
        if ("营业执照".equals(resp.getTitle()) && resp.getRecognizeWarnMsg().length == 0) {
            return new BusinessLicenseVo(
                    resp.getName(),
                    resp.getRegNum(),
                    resp.getCapital(),
                    resp.getPerson(),
                    resp.getAddress(),
                    resp.getBusiness(),
                    resp.getType(),
                    resp.getPeriod(),
                    resp.getSetDate(),
                    resp.getRegistrationDate(),
                    resp.getRegistrationAuthority()
            );
        } else {
            // 如果是非新版营业执照或其他问题，抛出异常
            String warnMsg = "";
            if (resp.getRecognizeWarnMsg().length > 0) {
                warnMsg = resp.getRecognizeWarnMsg()[0];
            } else if (!"营业执照".equals(resp.getTitle())) {
                warnMsg = "非新版营业执照";
            } else {
                warnMsg = "未知错误";
            }
            throw new BusinessLicenseException("非新版营业执照或黑白复印件或翻拍: " + warnMsg);
        }
    }
}
