package cn.hjf.job.company.service;

import cn.hjf.job.model.entity.company.CompanyTitle;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author hjf
 * @version 1.0
 * @description
 */

public interface CompanyTitleService extends IService<CompanyTitle> {

    /**
     * 设置公司管理员职称
     *
     * @param companyId 公司i d
     * @return 职称 id
     */
    Long setCompanyAdminTitle(Long companyId);

    /**
     * 获取职称名称根据 id
     *
     * @param titleId id
     * @return 职称
     */
    String findTitleNameById(Long titleId);

}
