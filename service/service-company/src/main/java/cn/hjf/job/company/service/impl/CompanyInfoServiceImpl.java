package cn.hjf.job.company.service.impl;

import cn.hjf.job.auth.client.UserRoleFeignClient;
import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.constant.UserRoleConstant;
import cn.hjf.job.common.minio.resolver.PublicFileUrlResolver;
import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.rabbit.service.RabbitService;
import cn.hjf.job.company.config.KeyProperties;
import cn.hjf.job.company.mapper.CompanyEmployeeMapper;
import cn.hjf.job.company.mapper.CompanyInfoMapper;
import cn.hjf.job.company.mapper.CompanySizeMapper;
import cn.hjf.job.company.repository.CompanyDescriptionRepository;
import cn.hjf.job.company.service.*;
import cn.hjf.job.model.document.company.CompanyDescriptionDoc;
import cn.hjf.job.model.dto.company.CompanyIdAndNameDTO;
import cn.hjf.job.model.entity.company.CompanyBusinessLicense;
import cn.hjf.job.model.entity.company.CompanyEmployee;
import cn.hjf.job.model.entity.company.CompanyInfo;
import cn.hjf.job.model.dto.company.CompanyInfoQuery;
import cn.hjf.job.model.entity.company.CompanySize;
import cn.hjf.job.model.form.company.CompanyBusinessLicenseForm;
import cn.hjf.job.model.form.company.CompanyInfoAndBusinessLicenseForm;
import cn.hjf.job.model.form.company.CompanyInfoForm;
import cn.hjf.job.model.request.auth.UserRoleRequest;
import cn.hjf.job.model.vo.company.CompanyInfoEsVo;
import cn.hjf.job.model.vo.company.CompanyInfoRecruiterVo;
import cn.hjf.job.model.vo.company.CompanyInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private PublicFileUrlResolver publicFileUrlResolver;

    @Resource
    private UserRoleFeignClient userRoleFeignClient;

    @Resource
    private KeyProperties keyProperties;

    @Resource
    private RedisTemplate<String, CompanyInfo> redisTemplate;

    @Resource
    private CompanySizeMapper companySizeMapper;

    @Resource(name = "companyIndustryServiceImpl")
    private CompanyIndustryService companyIndustryService;

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


        return new CompanyInfoQuery(companyInfo.getCompanyName(), publicFileUrlResolver.resolveSingleUrl(companyInfo.getCompanyLogo()), companyInfo.getStatus());
    }

    @Override
    public List<CompanyIdAndNameDTO> findCompanyIndexAndNameByName(String name) {
        LambdaQueryWrapper<CompanyInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CompanyInfo::getId, CompanyInfo::getCompanyName).eq(CompanyInfo::getStatus, 3).like(CompanyInfo::getCompanyName, name).last("LIMIT 10");

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

        // 设置当前用户为管理员角色
        UserRoleRequest userRoleRequest = new UserRoleRequest(userId, UserRoleConstant.ROLE_ADMIN_RECRUITER, keyProperties.getKey());
        userRoleFeignClient.setUserRole(userRoleRequest);

        Long companyId = companyInfo.getId();
        rabbitService.sendMessage(MqConst.EXCHANGE_COMPANY, MqConst.ROUTING_VALIDATE_COMPANY_BUSINESS_LICENSE, companyId);
    }

    @Override
    public boolean setCompanyStatus(Long companyId, Integer status) {
        LambdaUpdateWrapper<CompanyInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(CompanyInfo::getStatus, status)
                .eq(CompanyInfo::getId, companyId);
        int update = companyInfoMapper.update(lambdaUpdateWrapper);

        return update == 1;
    }

    @Override
    public CompanyInfoEsVo getCompanyInfoEsById(Long id) {
        CompanyInfoVo companyInfoById = getCompanyInfoById(id);
        CompanyInfoEsVo companyInfoEsVo = new CompanyInfoEsVo();
        BeanUtils.copyProperties(companyInfoById, companyInfoEsVo);
        return companyInfoEsVo;
    }

    @Override
    public CompanyInfoVo getCompanyInfoById(Long id) {
        // 从 Redis 缓存中查询 命中 就返回
        String redisKey = RedisConstant.COMPANY_INFO + id;
        CompanyInfo companyInfo = null;
        companyInfo = redisTemplate.opsForValue().get(redisKey);

        if (companyInfo == null) { // 没有命中缓存从 Mysql 中查询
            LambdaQueryWrapper<CompanyInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CompanyInfo::getId, id)
                    .eq(CompanyInfo::getStatus, 3);
            companyInfo = companyInfoMapper.selectOne(queryWrapper);

            redisTemplate.opsForValue().set(
                    redisKey,
                    companyInfo,
                    RedisConstant.COMPANY_INFO_TIME_OUT,
                    TimeUnit.SECONDS
            );
        }

        if (companyInfo == null) return null;

        CompanyInfoVo companyInfoVo = new CompanyInfoVo();

        BeanUtils.copyProperties(companyInfo, companyInfoVo);

        String rawLogo = publicFileUrlResolver.resolveSingleUrl(companyInfo.getCompanyLogo());

        companyInfoVo.setCompanyLogo(rawLogo);

        return companyInfoVo;
    }

    @Override
    public CompanyInfoRecruiterVo getCompanyInfoRecruiterVo(Long id) {
        // 获取当前管理员的公司 id
        Long companyId = companyEmployeeService.findCompanyIdByUserId(id);

        CompanyInfo companyInfo = companyInfoMapper.selectById(companyId);

        CompanyInfoRecruiterVo companyInfoRecruiterVo = new CompanyInfoRecruiterVo();
        BeanUtils.copyProperties(companyInfo, companyInfoRecruiterVo);

        // 设置公司 logo
        String rawLogo = publicFileUrlResolver.resolveSingleUrl(companyInfo.getCompanyLogo());
        companyInfoRecruiterVo.setCompanyLogo(rawLogo);

        // 设置公司描述
        Optional<CompanyDescriptionDoc> companyDescriptionDocResult = companyDescriptionRepository.findById(companyInfo.getCompanyDescription());
        companyDescriptionDocResult.ifPresent(companyDescriptionDoc -> companyInfoRecruiterVo.setCompanyDescription(companyDescriptionDoc.getDescription()));

        // 设置公司规模
        CompanySize companySize = companySizeMapper.selectById(companyInfo.getCompanySizeId());
        companyInfoRecruiterVo.setCompanySize(companySize.getSizeDescription());

        // 设置公司行业
        String industryDesc = companyIndustryService.getIndustryDesc(companyInfo.getIndustryId());
        companyInfoRecruiterVo.setIndustry(industryDesc);

        return companyInfoRecruiterVo;
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
