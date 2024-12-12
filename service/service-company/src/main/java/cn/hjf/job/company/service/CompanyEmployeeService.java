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

    /**
     * 获取加入公司的验证码
     *
     * @param userId 用户id
     * @return 验证码
     */
    String getVerificationCode(Long userId);

    /**
     * 添加员工到公司
     *
     * @param userId    用户 id
     * @param companyId 公司 id
     * @param code      验证码
     * @return 是否成功
     */
    boolean addEmployeeToCompany(Long userId, Long companyId, String code);
}
