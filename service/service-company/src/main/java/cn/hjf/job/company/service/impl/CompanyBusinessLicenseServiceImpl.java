package cn.hjf.job.company.service.impl;

import cn.hjf.job.company.mapper.CompanyBusinessLicenseMapper;
import cn.hjf.job.company.repository.BusinessScopeRepository;
import cn.hjf.job.company.service.CompanyBusinessLicenseService;
import cn.hjf.job.model.document.company.BusinessScopeDoc;
import cn.hjf.job.model.entity.company.CompanyBusinessLicense;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
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
public class CompanyBusinessLicenseServiceImpl extends ServiceImpl<CompanyBusinessLicenseMapper, CompanyBusinessLicense> implements CompanyBusinessLicenseService {

    @Resource
    private CompanyBusinessLicenseMapper companyBusinessLicenseMapper;

    @Resource
    private BusinessScopeRepository businessScopeRepository;

    @Override
    public boolean saveBusinessLicenseInfo(CompanyBusinessLicense companyBusinessLicense) {
        // 判断当前营业执照是否唯一,一个营业执照只能绑定一家公司
        LambdaQueryWrapper<CompanyBusinessLicense> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(CompanyBusinessLicense::getLicenseNumber)
                .eq(CompanyBusinessLicense::getLicenseNumber, companyBusinessLicense.getLicenseNumber());

        CompanyBusinessLicense businessLicense = companyBusinessLicenseMapper.selectOne(lambdaQueryWrapper);

        if (businessLicense != null) {
            throw new RuntimeException("当前营业执照已被注册");
        }

        // 保存 经营范围 MongoDB
        String businessScopeKey = saveBusinessScope(companyBusinessLicense.getBusinessScope());

        // 保存 营业执照
        companyBusinessLicense.setBusinessScope(businessScopeKey);
        companyBusinessLicense.setLegalPersonAuthStatus(0);
        int insert = companyBusinessLicenseMapper.insert(companyBusinessLicense);
        if (insert != 1) {
            throw new RuntimeException();
        }

        return true;
    }

    private String saveBusinessScope(String businessScope) {
        BusinessScopeDoc businessScopeDoc = new BusinessScopeDoc();
        businessScopeDoc.setBusinessScope(businessScope);
        BusinessScopeDoc save = businessScopeRepository.save(businessScopeDoc);
        return save.getId();
    }
}
