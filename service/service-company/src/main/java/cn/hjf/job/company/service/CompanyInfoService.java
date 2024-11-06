package cn.hjf.job.company.service;

import cn.hjf.job.model.entity.company.CompanyInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
public interface CompanyInfoService extends IService<CompanyInfo> {

    /**
     * 根据公司 ID 查询公司信息
     * @param id 公司ID
     * @return CompanyInfo 公司信息
     */
    CompanyInfo getCompanyInfoById(Integer id);

}
