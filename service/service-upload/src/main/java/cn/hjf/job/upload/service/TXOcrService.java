package cn.hjf.job.upload.service;

import cn.hjf.job.model.vo.company.BusinessLicenseVo;
import cn.hjf.job.model.vo.company.LegalPersonInfoVo;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;

/**
 * 腾讯云 OCR 服务
 *
 * @author hjf
 * @version 1.0
 * @description
 */

public interface TXOcrService {

    BusinessLicenseVo BizLicenseOCR(String imageBase64) throws TencentCloudSDKException;

    IDCardOCRResponse IDCardOCR(String imageBase64, String cardSide) throws TencentCloudSDKException;
}
