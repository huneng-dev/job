package cn.hjf.job.company.service;

import cn.hjf.job.model.entity.company.LegalPersonInfo;
import cn.hjf.job.model.vo.company.LegalPersonInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
public interface LegalPersonInfoService extends IService<LegalPersonInfo> {
    boolean saveLegalPersonInfo(LegalPersonInfoVo legalPersonInfoVo,Long uerId, Long companyId, Long businessLicenseId);
}
