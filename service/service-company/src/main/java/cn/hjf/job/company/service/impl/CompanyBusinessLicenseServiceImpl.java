package cn.hjf.job.company.service.impl;

import cn.hjf.job.company.mapper.CompanyBusinessLicenseMapper;
import cn.hjf.job.company.repository.BusinessScopeRepository;
import cn.hjf.job.company.service.CompanyBusinessLicenseService;
import cn.hjf.job.model.document.company.BusinessScopeDoc;
import cn.hjf.job.model.entity.company.CompanyBusinessLicense;
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
