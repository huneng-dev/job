package cn.hjf.job.common.tx.util;

import cn.hjf.job.common.tx.config.TencentCloudProperties;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.BizLicenseOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.BizLicenseOCRResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class OcrUtil {

    @Resource
    private TencentCloudProperties tencentCloudProperties;

    public BizLicenseOCRResponse BizLicenseOCR(String imageBase64) throws TencentCloudSDKException {
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

        // 执行OCR请求]
        return client.BizLicenseOCR(req);
    }
}
