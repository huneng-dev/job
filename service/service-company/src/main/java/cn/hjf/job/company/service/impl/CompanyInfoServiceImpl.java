package cn.hjf.job.company.service.impl;

import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.rabbit.service.RabbitService;
import cn.hjf.job.company.mapper.CompanyEmployeeMapper;
import cn.hjf.job.company.mapper.CompanyInfoMapper;
import cn.hjf.job.company.repository.CompanyDescriptionRepository;
import cn.hjf.job.company.service.CompanyBusinessLicenseService;
import cn.hjf.job.company.service.CompanyEmployeeService;
import cn.hjf.job.company.service.CompanyInfoService;
import cn.hjf.job.company.service.CompanyTitleService;
import cn.hjf.job.model.document.company.CompanyDescriptionDoc;
import cn.hjf.job.model.dto.company.CompanyIdAndNameDTO;
import cn.hjf.job.model.entity.company.CompanyBusinessLicense;
import cn.hjf.job.model.entity.company.CompanyEmployee;
import cn.hjf.job.model.entity.company.CompanyInfo;
import cn.hjf.job.model.dto.company.CompanyInfoQuery;
import cn.hjf.job.model.form.company.CompanyBusinessLicenseForm;
import cn.hjf.job.model.form.company.CompanyInfoAndBusinessLicenseForm;
import cn.hjf.job.model.form.company.CompanyInfoForm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
@Service
public class CompanyInfoServiceImpl extends ServiceImpl<CompanyInfoMapper, CompanyInfo> implements CompanyInfoService {

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private CompanyEmployeeMapper companyEmployeeMapper;

    @Resource
    private CompanyDescriptionRepository companyDescriptionRepository;

    @Resource(name = "companyBusinessLicenseServiceImpl")
    private CompanyBusinessLicenseService companyBusinessLicenseService;

    @Resource(name = "companyTitleServiceImpl")
    private CompanyTitleService companyTitleService;

    @Resource(name = "companyEmployeeServiceImpl")
    private CompanyEmployeeService companyEmployeeService;

    @Resource
    private RabbitService rabbitService;


    @Override
    public CompanyInfoQuery findCompanyInfoByUserId(Long userId) {

        // 查询用户所在的公司 id
        LambdaQueryWrapper<CompanyEmployee> queryWrapperCompanyEmployee = new LambdaQueryWrapper<>();
        queryWrapperCompanyEmployee.select(CompanyEmployee::getCompanyId, CompanyEmployee::getIsAdmin).eq(CompanyEmployee::getUserId, userId);
        CompanyEmployee companyEmployee = companyEmployeeMapper.selectOne(queryWrapperCompanyEmployee);

        if (companyEmployee == null) {
            return null;
        }

        // 查询公司信息
        LambdaQueryWrapper<CompanyInfo> queryWrapperCompanyInfo = new LambdaQueryWrapper<>();
        queryWrapperCompanyInfo.select(CompanyInfo::getCompanyName, CompanyInfo::getCompanyLogo, CompanyInfo::getStatus).eq(CompanyInfo::getId, companyEmployee.getCompanyId());

        CompanyInfo companyInfo = companyInfoMapper.selectOne(queryWrapperCompanyInfo);

        if (companyInfo == null) {
            return null;
        }

        return new CompanyInfoQuery(companyInfo.getCompanyName(), companyInfo.getCompanyLogo(), companyInfo.getStatus());
    }

    @Override
    public List<CompanyIdAndNameDTO> findCompanyIndexAndNameByName(String name) {
        LambdaQueryWrapper<CompanyInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CompanyInfo::getId, CompanyInfo::getCompanyName).eq(CompanyInfo::getStatus, 2).like(CompanyInfo::getCompanyName, name).last("LIMIT 10");

        List<CompanyInfo> companyInfos = companyInfoMapper.selectList(queryWrapper);

        return companyInfos.stream().map(companyInfo -> new CompanyIdAndNameDTO(companyInfo.getId(), companyInfo.getCompanyName())).toList();
    }

    @Override
    @GlobalTransactional(name = "save-company-business-license", rollbackFor = Exception.class)
    public void saveCompanyInfoAndBusinessLicense(CompanyInfoAndBusinessLicenseForm companyInfoAndBusinessLicenseForm, Long userId) {
        // 前置条件校验
        CompanyInfoQuery companyInfoByUserId = findCompanyInfoByUserId(userId);
        if (companyInfoByUserId != null) throw new RuntimeException("已有公司,无法注册");

        // 提取数据
        CompanyInfoForm companyInfoForm = companyInfoAndBusinessLicenseForm.getCompanyInfoForm();
        CompanyBusinessLicenseForm companyBusinessLicenseForm = companyInfoAndBusinessLicenseForm.getCompanyBusinessLicenseForm();

        // 保存公司描述 (MongoDB)
        String companyDescKey = saveCompanyDescriptionToMongoDB(companyInfoForm.getCompanyDescription());
        companyInfoForm.setCompanyDescription(companyDescKey);
        // 保存公司信息 (MySQL)
        CompanyInfo companyInfo = new CompanyInfo();
        BeanUtils.copyProperties(companyInfoForm, companyInfo);
        companyInfo.setCount(1);
        companyInfo.setStatus(0);
        int inserted = companyInfoMapper.insert(companyInfo);
        // 如果不等于 1 表示插入失败 抛出异常回滚事务
        if (inserted != 1) {
            throw new RuntimeException();
        }

        // 保存公司营业执照
        CompanyBusinessLicense companyBusinessLicense = new CompanyBusinessLicense();
        BeanUtils.copyProperties(companyBusinessLicenseForm, companyBusinessLicense);
        companyBusinessLicense.setCompanyId(companyInfo.getId());
        boolean b = companyBusinessLicenseService.saveBusinessLicenseInfo(companyBusinessLicense);
        if (!b) {
            throw new RuntimeException();
        }

        // 设置公司的初始员工,管理员用户
        Long titleId = companyTitleService.setCompanyAdminTitle(companyInfo.getId());

        boolean b1 = companyEmployeeService.setCompanyAdminEmployee(companyInfo.getId(), userId, titleId);

        if (!b1) throw new RuntimeException();

        Long companyId = companyInfo.getId();
        rabbitService.sendMessage(MqConst.EXCHANGE_COMPANY, MqConst.ROUTING_VALIDATE_COMPANY_BUSINESS_LICENSE, companyId);
    }

    /**
     * 保存公司描述到 MongoDB
     *
     * @param description 公司描述
     * @return key
     */
    private String saveCompanyDescriptionToMongoDB(String description) {
        CompanyDescriptionDoc companyDescriptionDoc = new CompanyDescriptionDoc();
        companyDescriptionDoc.setDescription(description);
        CompanyDescriptionDoc save = companyDescriptionRepository.save(companyDescriptionDoc);
        return save.getId();
    }
}
