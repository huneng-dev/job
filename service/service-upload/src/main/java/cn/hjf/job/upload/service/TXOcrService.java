package cn.hjf.job.upload.service;

import cn.hjf.job.model.vo.company.BusinessLicenseVo;
import cn.hjf.job.model.vo.company.LegalPersonInfoVo;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

/**
 * 腾讯云 OCR 服务
 *
 * @author hjf
 * @version 1.0
 * @description
 */

public interface TXOcrService {

    BusinessLicenseVo BizLicenseOCR(String imageBase64) throws TencentCloudSDKException;

    LegalPersonInfoVo IDCardOCR(String imageBase64,String cardSide) throws TencentCloudSDKException;
}
