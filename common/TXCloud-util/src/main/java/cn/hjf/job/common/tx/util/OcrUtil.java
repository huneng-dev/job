package cn.hjf.job.common.tx.util;

import cn.hjf.job.common.tx.config.TencentCloudProperties;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.BizLicenseOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.BizLicenseOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
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


    public IDCardOCRResponse IDCardOCR(String imageBase64, String cardSide) throws TencentCloudSDKException {
        Credential cred = new Credential(tencentCloudProperties.getSecretId(), tencentCloudProperties.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ocr.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        OcrClient client = new OcrClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        IDCardOCRRequest req = new IDCardOCRRequest();
        req.setImageBase64(imageBase64);
        req.setCardSide(cardSide);

        // 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
        return client.IDCardOCR(req);
    }
}
