package cn.hjf.job.upload.service.impl;

import cn.hjf.job.common.tx.util.OcrUtil;
import cn.hjf.job.model.vo.company.BusinessLicenseVo;
import cn.hjf.job.model.vo.company.LegalPersonInfoVo;
import com.tencentcloudapi.ocr.v20181119.models.BizLicenseOCRResponse;
import cn.hjf.job.upload.exception.BusinessLicenseException;
import cn.hjf.job.upload.service.TXOcrService;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author hjf
 * @version 1.0
 * @description
 */
@Service
public class TXOcrServiceImpl implements TXOcrService {

    @Resource
    private OcrUtil ocrUtil;

    @Override
    public BusinessLicenseVo BizLicenseOCR(String imageBase64) throws TencentCloudSDKException {

        BizLicenseOCRResponse resp = ocrUtil.BizLicenseOCR(imageBase64);

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

    @Override
    public LegalPersonInfoVo IDCardOCR(String imageBase64, String cardSide) throws TencentCloudSDKException {
        IDCardOCRResponse idCardOCRResponse = ocrUtil.IDCardOCR(imageBase64, cardSide);
        LegalPersonInfoVo legalPersonInfoVo = new LegalPersonInfoVo();
        legalPersonInfoVo.setName(idCardOCRResponse.getName());
        legalPersonInfoVo.setGender(Objects.equals("男", idCardOCRResponse.getSex()) ? 1 : 2);

        // 定义格式化器，解析 yyyy/M/d 格式的日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");

        // 将字符串转换为 LocalDate
        LocalDate birthDate = LocalDate.parse(idCardOCRResponse.getBirth(), formatter);
        legalPersonInfoVo.setBirthday(birthDate);
        legalPersonInfoVo.setIdcardAddress(idCardOCRResponse.getAddress());
        legalPersonInfoVo.setIdcardNo(idCardOCRResponse.getIdNum());
        return legalPersonInfoVo;
    }
}
