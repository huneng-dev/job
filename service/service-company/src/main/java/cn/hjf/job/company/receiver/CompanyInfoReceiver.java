package cn.hjf.job.company.receiver;

import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.tx.util.Base64Util;
import cn.hjf.job.common.tx.util.OcrUtil;
import cn.hjf.job.company.mapper.CompanyBusinessLicenseMapper;
import cn.hjf.job.company.mapper.CompanyInfoMapper;
import cn.hjf.job.company.repository.BusinessScopeRepository;
import cn.hjf.job.model.document.company.BusinessScopeDoc;
import cn.hjf.job.model.entity.company.CompanyBusinessLicense;
import cn.hjf.job.model.entity.company.CompanyInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rabbitmq.client.Channel;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.models.BizLicenseOCRResponse;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class CompanyInfoReceiver {

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private CompanyBusinessLicenseMapper companyBusinessLicenseMapper;

    @Resource
    private MinioClient minioClient;

    @Resource
    private OcrUtil ocrUtil;

    @Resource
    private BusinessScopeRepository businessScopeRepository;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConst.QUEUE_VALIDATE_COMPANY_BUSINESS_LICENSE, durable = "true"), exchange = @Exchange(value = MqConst.EXCHANGE_COMPANY), key = {MqConst.ROUTING_VALIDATE_COMPANY_BUSINESS_LICENSE}))
    public void validateCompanyInfoAndBusinessLicense(Long companyId, Message message, Channel channel) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, TencentCloudSDKException {
        // 获取当前公司的信息
        LambdaQueryWrapper<CompanyInfo> queryWrapperByCompanyInfo = new LambdaQueryWrapper<>();
        queryWrapperByCompanyInfo.eq(CompanyInfo::getId, companyId).eq(CompanyInfo::getStatus, 0);
        CompanyInfo companyInfo = companyInfoMapper.selectOne(queryWrapperByCompanyInfo);
        if (companyInfo == null) return;

        // TODO 将关键数据打包上传到数据万象,校验是否有违规,有违规就设置认证失败

        // 获取营业执照的信息
        LambdaQueryWrapper<CompanyBusinessLicense> queryWrapperByBusinessLicense = new LambdaQueryWrapper<>();
        queryWrapperByBusinessLicense.eq(CompanyBusinessLicense::getCompanyId, companyId).eq(CompanyBusinessLicense::getLegalPersonAuthStatus, 0);
        CompanyBusinessLicense companyBusinessLicense = companyBusinessLicenseMapper.selectOne(queryWrapperByBusinessLicense);
        if (companyBusinessLicense == null) return;

        String businessScope = getBusinessScope(companyBusinessLicense);

        // 获取 String image64 通过path
        String fullPath = companyBusinessLicense.getBusinessLicenseUrl();
        // 移除路径开头的斜杠（/）并分割
        String[] parts = fullPath.substring(1).split("/", 2);  // 去除第一个斜杠后根据第二个斜杠分割
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid path format. Expected '/bucketName/path/to/file'");
        }
        String bucketName = parts[0];  // 桶名
        String filePath = parts[1];    // 文件路径

        InputStream file = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(filePath).build());
        String image64 = Base64Util.convertInputStreamToBase64(file);

        BizLicenseOCRResponse resp = ocrUtil.BizLicenseOCR(image64);

        if (!companyBusinessLicense.getName().equals(resp.getName())) return;
        if (!companyBusinessLicense.getLicenseNumber().equals(resp.getRegNum())) return;
        if (!companyBusinessLicense.getCapital().equals(resp.getCapital())) return;
        if (!companyBusinessLicense.getLegalPerson().equals(resp.getPerson())) return;
        if (!companyBusinessLicense.getAddress().equals(resp.getAddress())) return;
        if (!businessScope.equals(resp.getBusiness())) return;
        if (!companyBusinessLicense.getType().equals(resp.getType())) return;
        if (!companyBusinessLicense.getPeriod().equals(resp.getPeriod())) return;
        if (!companyBusinessLicense.getEstablishmentDate().equals(resp.getSetDate())) return;
        if (!companyBusinessLicense.getRegistrationDate().equals(resp.getRegistrationDate())) return;
        if (!companyBusinessLicense.getRegistrationAuthority().equals(resp.getRegistrationAuthority())) return;

        companyBusinessLicense.setIsCopy(resp.getIsDuplication() == 1L ? 1 : 0);

        if (resp.getElectronic()) {
            companyBusinessLicense.setIsElectronic(1);
        } else {
            companyBusinessLicense.setIsElectronic(0);
        }

        if (resp.getSeal()) {
            companyBusinessLicense.setHasSeal(1);
        } else {
            companyBusinessLicense.setHasSeal(0);
        }

        // 设置状态
        companyInfo.setStatus(1);

        // 保存信息
        companyInfoMapper.updateById(companyInfo);
        companyBusinessLicenseMapper.updateById(companyBusinessLicense);
    }


    public String getBusinessScope(CompanyBusinessLicense companyBusinessLicense) {
        BusinessScopeDoc businessScopeDoc = businessScopeRepository.findById(companyBusinessLicense.getBusinessScope())
                .orElseThrow(() -> new IllegalArgumentException("BusinessScopeDoc not found for id: " + companyBusinessLicense.getBusinessScope()));
        return businessScopeDoc.getBusinessScope();
    }
}
