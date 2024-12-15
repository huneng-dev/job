package cn.hjf.job.position.service.impl;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.client.CompanyEmployeeFeignClient;
import cn.hjf.job.model.document.position.PositionDescriptionDoc;
import cn.hjf.job.model.entity.position.PositionInfo;
import cn.hjf.job.model.form.position.PositionInfoForm;
import cn.hjf.job.position.mapper.PositionInfoMapper;
import cn.hjf.job.position.repository.PositionDescriptionRepository;
import cn.hjf.job.position.service.PositionInfoService;
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
public class PositionInfoServiceImpl extends ServiceImpl<PositionInfoMapper, PositionInfo> implements PositionInfoService {

    @Resource
    private PositionInfoMapper positionInfoMapper;

    @Resource
    private CompanyEmployeeFeignClient companyEmployeeFeignClient;

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
}
