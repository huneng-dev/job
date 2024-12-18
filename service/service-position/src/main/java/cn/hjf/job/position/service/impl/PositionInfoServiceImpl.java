package cn.hjf.job.position.service.impl;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.client.CompanyAddressFeignClient;
import cn.hjf.job.company.client.CompanyEmployeeFeignClient;
import cn.hjf.job.model.document.position.PositionDescriptionDoc;
import cn.hjf.job.model.entity.position.PositionInfo;
import cn.hjf.job.model.form.position.PositionInfoForm;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.company.AddressInfoVo;
import cn.hjf.job.model.vo.company.CompanyEmployeeVo;
import cn.hjf.job.model.vo.company.CompanyIdAndIsAdmin;
import cn.hjf.job.model.vo.position.RecruiterBasePositionInfoVo;
import cn.hjf.job.model.vo.position.RecruiterPositionInfoVo;
import cn.hjf.job.position.mapper.PositionInfoMapper;
import cn.hjf.job.position.repository.PositionDescriptionRepository;
import cn.hjf.job.position.service.PositionInfoService;
import cn.hjf.job.position.service.PositionTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class PositionInfoServiceImpl extends ServiceImpl<PositionInfoMapper, PositionInfo> implements PositionInfoService {

    @Resource
    private PositionInfoMapper positionInfoMapper;

    @Resource
    private CompanyEmployeeFeignClient companyEmployeeFeignClient;

    @Resource
    private CompanyAddressFeignClient companyAddressFeignClient;

    @Resource(name = "positionTypeServiceImpl")
    private PositionTypeService positionTypeService;

    @Resource
    private PositionDescriptionRepository positionDescriptionRepository;

    @Override
    public boolean create(PositionInfoForm positionInfoForm, Long userId) {
        // 获取当前用户的公司的 id
        Result<Long> result = companyEmployeeFeignClient.findCompanyIdByUserId();
        if (!result.getCode().equals(200)) {
            return false;
        }
        Long companyId = result.getData();

        PositionDescriptionDoc positionDescriptionDoc = new PositionDescriptionDoc();
        positionDescriptionDoc.setDescription(positionInfoForm.getPositionDescription());
        PositionDescriptionDoc descriptionDoc = positionDescriptionRepository.save(positionDescriptionDoc);

        // 准备数据
        PositionInfo positionInfo = new PositionInfo();
        BeanUtils.copyProperties(positionInfoForm, positionInfo);
        positionInfo.setCompanyId(companyId);
        positionInfo.setCreatorId(userId);
        positionInfo.setPositionDescription(descriptionDoc.getId());

        /*
        设置状态为 待开放 3
        一般情况下将 职位名称，职位描述，上传到 腾讯云数据万象 进行审核。
        在进行人工审核后，设置为 待开放
        此处省略以上流程，直接设置为 3
         */

        positionInfo.setStatus(3);
        int insert = positionInfoMapper.insert(positionInfo);

        return insert == 1;
    }

    @Override
    public PageVo<RecruiterBasePositionInfoVo> findRecruiterBasePositionInfoByUserId(Page<PositionInfo> positionInfoPage, String positionName, Integer status, Long userId) {
        // 查询当前用户是否是管理员 管理员查询全部职位,非管理员查询的自己创建的
        Result<CompanyIdAndIsAdmin> companyIdAndIsAdminByUserId = companyEmployeeFeignClient.findCompanyIdAndIsAdminByUserId();
        CompanyIdAndIsAdmin companyIdAndIsAdmin = companyIdAndIsAdminByUserId.getData();
        Long companyId = companyIdAndIsAdmin.getCompanyId();
        Integer isAdmin = companyIdAndIsAdmin.getIsAdmin();

        LambdaQueryWrapper<PositionInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(PositionInfo::getId, PositionInfo::getPositionName, PositionInfo::getEducationRequirement, PositionInfo::getExperienceRequirement, PositionInfo::getMinSalary, PositionInfo::getMaxSalary, PositionInfo::getStatus, PositionInfo::getWatchCount, PositionInfo::getCommunicationCount, PositionInfo::getFavoriteCount).eq(PositionInfo::getCompanyId, companyId).orderByDesc(PositionInfo::getId);


        // 设置状态 0 标识没有状态查询全部
        if (!status.equals(0)) {
            queryWrapper.eq(PositionInfo::getStatus, status);
        }

        // 如果不是管理员就设置 创建人 id
        if (isAdmin.equals(0)) {
            queryWrapper.eq(PositionInfo::getCreatorId, userId);
        }

        // 设置模糊查询
        if (!positionName.isEmpty()) {
            queryWrapper.like(PositionInfo::getPositionName, positionName);
        }


        // 分页查询
        Page<PositionInfo> selectPage = positionInfoMapper.selectPage(positionInfoPage, queryWrapper);

        List<PositionInfo> records = selectPage.getRecords();

        List<RecruiterBasePositionInfoVo> recruiterBasePositionInfoVos = records.stream().map(positionInfo -> new RecruiterBasePositionInfoVo(positionInfo.getId(), positionInfo.getPositionName(), positionInfo.getEducationRequirement(), positionInfo.getExperienceRequirement(), positionInfo.getMinSalary(), positionInfo.getMaxSalary(), positionInfo.getStatus(), positionInfo.getWatchCount(), positionInfo.getCommunicationCount(), positionInfo.getFavoriteCount())).toList();

        PageVo<RecruiterBasePositionInfoVo> recruiterBasePositionInfoVoPageVo = new PageVo<>();
        recruiterBasePositionInfoVoPageVo.setRecords(recruiterBasePositionInfoVos);
        recruiterBasePositionInfoVoPageVo.setLimit(selectPage.getSize());
        recruiterBasePositionInfoVoPageVo.setPages(selectPage.getPages());
        recruiterBasePositionInfoVoPageVo.setPage(selectPage.getCurrent());
        recruiterBasePositionInfoVoPageVo.setTotal(selectPage.getTotal());
        return recruiterBasePositionInfoVoPageVo;
    }

    @Override
    public RecruiterPositionInfoVo getRecruiterPositionInfoVoById(Long positionId, Long userId) {
        // 获取公司id
        Result<Long> result = companyEmployeeFeignClient.findCompanyIdByUserId();

        Long companyId = result.getData();

        LambdaQueryWrapper<PositionInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PositionInfo::getId, positionId).eq(PositionInfo::getCompanyId, companyId);

        // 职位信息
        PositionInfo positionInfo = positionInfoMapper.selectOne(queryWrapper);
        // 结果职位信息
        RecruiterPositionInfoVo recruiterPositionInfoVo = new RecruiterPositionInfoVo();
        recruiterPositionInfoVo.setId(positionId);

        // 获取创建人与负责人信息
        Result<CompanyEmployeeVo> creatorResult = companyEmployeeFeignClient.findCompanyEmployeeById(positionInfo.getCreatorId());
        Result<CompanyEmployeeVo> responsibleResult = companyEmployeeFeignClient.findCompanyEmployeeById(positionInfo.getResponsibleId());
        recruiterPositionInfoVo.setCreator(creatorResult.getData());
        recruiterPositionInfoVo.setResponsible(responsibleResult.getData());

        // 获取地址
        Result<AddressInfoVo> addressInfoVoResult = companyAddressFeignClient.getAddressById(positionInfo.getAddressId());
        recruiterPositionInfoVo.setAddress(addressInfoVoResult.getData());

        // 获取职位类型描述
        String positionTypeDesc = positionTypeService.getPositionTypeDescByPositionId(positionInfo.getPositionTypeId());
        recruiterPositionInfoVo.setPositionTypeDesc(positionTypeDesc);

        // 设置职位名称
        recruiterPositionInfoVo.setPositionName(positionInfo.getPositionName());

        // 设置职位描述
        Optional<PositionDescriptionDoc> optional = positionDescriptionRepository.findById(positionInfo.getPositionDescription());
        optional.ifPresent(positionDescriptionDoc -> recruiterPositionInfoVo.setPositionDescription(positionDescriptionDoc.getDescription()));

        // 设置 职位类型,
        recruiterPositionInfoVo.setPositionType(positionInfo.getPositionType());
        recruiterPositionInfoVo.setEducationRequirement(positionInfo.getEducationRequirement());
        recruiterPositionInfoVo.setExperienceRequirement(positionInfo.getExperienceRequirement());
        recruiterPositionInfoVo.setDailyWorkHours(positionInfo.getDailyWorkHours());
        recruiterPositionInfoVo.setWeeklyWorkDays(positionInfo.getWeeklyWorkDays());
        recruiterPositionInfoVo.setMinSalary(positionInfo.getMinSalary());
        recruiterPositionInfoVo.setMaxSalary(positionInfo.getMaxSalary());
        recruiterPositionInfoVo.setStatus(positionInfo.getStatus());
        recruiterPositionInfoVo.setWatchCount(positionInfo.getWatchCount());
        recruiterPositionInfoVo.setCommunicationCount(positionInfo.getCommunicationCount());
        recruiterPositionInfoVo.setFavoriteCount(positionInfo.getFavoriteCount());

        return recruiterPositionInfoVo;
    }
}
