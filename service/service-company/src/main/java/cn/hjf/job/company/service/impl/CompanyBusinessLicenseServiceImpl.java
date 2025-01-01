package cn.hjf.job.company.service.impl;

import cn.hjf.job.common.minio.resolver.PrivateFileUrlResolver;
import cn.hjf.job.company.mapper.CompanyBusinessLicenseMapper;
import cn.hjf.job.company.repository.BusinessScopeRepository;
import cn.hjf.job.company.service.CompanyBusinessLicenseService;
import cn.hjf.job.model.document.company.BusinessScopeDoc;
import cn.hjf.job.model.entity.company.CompanyBusinessLicense;
import cn.hjf.job.model.vo.company.CompanyBusinessLicenseRecruiterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Resource
    private PrivateFileUrlResolver privateFileUrlResolver;

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

    @Override
    public boolean setBusinessLicenseLegalPersonId(Long businessLicenseId, Long legalPersonId) {
        LambdaUpdateWrapper<CompanyBusinessLicense> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(CompanyBusinessLicense::getLegalPersonId, legalPersonId)
                .eq(CompanyBusinessLicense::getId, businessLicenseId);

        int update = companyBusinessLicenseMapper.update(lambdaUpdateWrapper);
        return update == 1;
    }

    @Override
    public boolean setBusinessLicenseLegalPersonStatus(Long businessLicenseId, Integer legalPersonStatus) {
        LambdaUpdateWrapper<CompanyBusinessLicense> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(CompanyBusinessLicense::getLegalPersonAuthStatus, legalPersonStatus)
                .eq(CompanyBusinessLicense::getId, businessLicenseId);
        int update = companyBusinessLicenseMapper.update(lambdaUpdateWrapper);
        return update == 1;
    }

    @Override
    public Long findBusinessLicenseIdByCompanyId(Long companyId) {
        LambdaQueryWrapper<CompanyBusinessLicense> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(CompanyBusinessLicense::getId)
                .eq(CompanyBusinessLicense::getCompanyId, companyId);
        CompanyBusinessLicense companyBusinessLicense = companyBusinessLicenseMapper.selectOne(lambdaQueryWrapper);
        if (companyBusinessLicense == null) return null;
        return companyBusinessLicense.getId();
    }

    @Override
    public CompanyBusinessLicenseRecruiterVo getCompanyBusinessLicenseRecruiterVo(Long companyId) {

        LambdaQueryWrapper<CompanyBusinessLicense> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CompanyBusinessLicense::getCompanyId, companyId);
        CompanyBusinessLicense companyBusinessLicense = companyBusinessLicenseMapper.selectOne(lambdaQueryWrapper);

        CompanyBusinessLicenseRecruiterVo companyBusinessLicenseRecruiterVo = new CompanyBusinessLicenseRecruiterVo();
        BeanUtils.copyProperties(companyBusinessLicense, companyBusinessLicenseRecruiterVo);
        Optional<BusinessScopeDoc> businessScopeDocOptional = businessScopeRepository.findById(companyBusinessLicense.getBusinessScope());
        businessScopeDocOptional.ifPresent(businessScopeDoc -> companyBusinessLicenseRecruiterVo.setBusinessScope(businessScopeDoc.getBusinessScope()));
        return companyBusinessLicenseRecruiterVo;
    }

    @Override
    public String getBusinessLicenseUrl(Long companyId) {
        LambdaQueryWrapper<CompanyBusinessLicense> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(CompanyBusinessLicense::getBusinessLicenseUrl).eq(CompanyBusinessLicense::getCompanyId, companyId);
        CompanyBusinessLicense companyBusinessLicense = companyBusinessLicenseMapper.selectOne(lambdaQueryWrapper);
        if (companyBusinessLicense == null) return null;
        return privateFileUrlResolver.resolveSingleUrl(companyBusinessLicense.getBusinessLicenseUrl(), 300);
    }

    private String saveBusinessScope(String businessScope) {
        BusinessScopeDoc businessScopeDoc = new BusinessScopeDoc();
        businessScopeDoc.setBusinessScope(businessScope);
        BusinessScopeDoc save = businessScopeRepository.save(businessScopeDoc);
        return save.getId();
    }
}
