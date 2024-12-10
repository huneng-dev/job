package cn.hjf.job.company.service;

import cn.hjf.job.model.entity.company.CompanyEmployee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author hjf
 * @version 1.0
 * @description
 */

public interface CompanyEmployeeService extends IService<CompanyEmployee> {

    /**
     * 设置当前公司的管理员
     *
     * @param companyId 公司 id
     * @param userId    用户 id
     * @param titleId   职称 id
     * @return 是否执行成功
     */
    boolean setCompanyAdminEmployee(Long companyId, Long userId, Long titleId);

    /**
     * 获取公司 id
     *
     * @param userId 用户 id
     * @return 公司 id
     */
    Long findCompanyIdByUserId(Long userId);
}
