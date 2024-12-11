package cn.hjf.job.company.service.impl;

import cn.hjf.job.company.mapper.LegalPersonInfoMapper;
import cn.hjf.job.company.service.CompanyBusinessLicenseService;
import cn.hjf.job.company.service.CompanyInfoService;
import cn.hjf.job.company.service.LegalPersonInfoService;
import cn.hjf.job.model.entity.company.LegalPersonInfo;
import cn.hjf.job.model.form.user.UserIdCardInfoForm;
import cn.hjf.job.model.vo.company.LegalPersonInfoVo;
import cn.hjf.job.user.client.UserInfoFeignClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
@Service
public class LegalPersonInfoServiceImpl extends ServiceImpl<LegalPersonInfoMapper, LegalPersonInfo> implements LegalPersonInfoService {

    @Resource
    private LegalPersonInfoMapper legalPersonInfoMapper;

    @Resource(name = "companyBusinessLicenseServiceImpl")
    private CompanyBusinessLicenseService companyBusinessLicenseService;

    @Resource(name = "companyInfoServiceImpl")
    private CompanyInfoService companyInfoService;

    @Resource
    private UserInfoFeignClient userInfoFeignClient;

    @Override
    public boolean saveLegalPersonInfo(LegalPersonInfoVo legalPersonInfoVo, Long uerId, Long companyId, Long businessLicenseId) {
        // TODO 对比当前公司的营业执照法人姓名是否一致
        // TODO 检查身份证是否过期
        // TODO 分布式锁与分布式事务

        // 查询当前企业法人信息是否存在
        LambdaQueryWrapper<LegalPersonInfo> queryWrapperByLegalPerson = new LambdaQueryWrapper<>();
        queryWrapperByLegalPerson.select(LegalPersonInfo::getId, LegalPersonInfo::getIdcardNo, LegalPersonInfo::getCompanyCount)
                .eq(LegalPersonInfo::getIdcardNo, legalPersonInfoVo.getIdcardNo());

        LegalPersonInfo legalPersonInfo = legalPersonInfoMapper.selectOne(queryWrapperByLegalPerson);

        if (legalPersonInfo == null) {
            // 执行法人不存在流程
            // TODO 将身份证图片二次进行 OCR 识别,防止伪造信息
            // 设置法人信息
            LegalPersonInfo legalPerson = new LegalPersonInfo();
            BeanUtils.copyProperties(legalPersonInfoVo, legalPerson);
            legalPerson.setCompanyCount(1);
            legalPersonInfoMapper.insert(legalPerson);

            // 设置营业执照法人 id
            companyBusinessLicenseService.setBusinessLicenseLegalPersonId(businessLicenseId, legalPerson.getId());

        } else {
            // 执行法人存在流程
            // TODO 提取法人信息与 legalPersonInfoVo 验证 是否一致
            // 更新 法人名下的公司数量
            LambdaUpdateWrapper<LegalPersonInfo> lambdaUpdateWrapperByLegalPerson = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapperByLegalPerson.set(LegalPersonInfo::getCompanyCount, legalPersonInfo.getCompanyCount() + 1)
                    .eq(LegalPersonInfo::getId, legalPersonInfo.getId());

            // 设置营业执照法人 id
            companyBusinessLicenseService.setBusinessLicenseLegalPersonId(businessLicenseId, legalPersonInfo.getId());

        }

        /*
          一般情况下设置营业执照下的法人认证状态为 等待人脸核身验证,
          但是目前无法实现人脸核身,
          所以直接设置企业认证状态为已认证.
         */

        // 设置 营业执照状态为已认证
        companyBusinessLicenseService.setBusinessLicenseLegalPersonStatus(businessLicenseId, 2);
        // 设置 公司为已认证
        companyInfoService.setCompanyStatus(companyId, 3);
        // 设置 用户状态为已认证(认证信息为当前法人)
        UserIdCardInfoForm userIdCardInfoForm = new UserIdCardInfoForm(
                legalPersonInfoVo.getName(),
                legalPersonInfoVo.getGender(),
                legalPersonInfoVo.getBirthday(),
                legalPersonInfoVo.getIdcardNo(),
                legalPersonInfoVo.getIdcardAddress(),
                legalPersonInfoVo.getIdcardExpire(),
                legalPersonInfoVo.getIdcardFrontUrl(),
                legalPersonInfoVo.getIdcardBackUrl()
        );
        userInfoFeignClient.setUserIdCardInfo(userIdCardInfoForm);

        return true;
    }
}
