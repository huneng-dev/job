package cn.hjf.job.company.service;

import cn.hjf.job.model.entity.company.CompanyBusinessLicense;
import cn.hjf.job.model.vo.company.CompanyBusinessLicenseRecruiterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
public interface CompanyBusinessLicenseService extends IService<CompanyBusinessLicense> {

    /**
     * 保存营业执照 (只能初始化公司时调用)
     *
     * @param companyBusinessLicense 营业执照
     * @return 是否成功
     */
    boolean saveBusinessLicenseInfo(CompanyBusinessLicense companyBusinessLicense);

    /**
     * 设置企业营业执照的法人 id
     *
     * @param legalPersonId 法人 id
     * @return 是否成功
     */
    boolean setBusinessLicenseLegalPersonId(Long businessLicenseId, Long legalPersonId);

    /**
     * 设置营业执照的法人认证状态
     *
     * @param businessLicenseId 营业执照 id
     * @param legalPersonStatus 法人 id
     * @return 是否成功
     */
    boolean setBusinessLicenseLegalPersonStatus(Long businessLicenseId, Integer legalPersonStatus);

    /**
     * 根据公司 id 获取 营业执照 id
     *
     * @param companyId 公司 id
     * @return 营业执照 id
     */
    Long findBusinessLicenseIdByCompanyId(Long companyId);

    /**
     * 获取营业执照信息招聘端 (管理员)
     *
     * @param companyId 用户id
     * @return CompanyBusinessLicenseRecruiterVo
     */
    CompanyBusinessLicenseRecruiterVo getCompanyBusinessLicenseRecruiterVo(Long companyId);

    /**
     * 获取营业执照照片
     *
     * @param companyId 公司 id
     * @return String
     */
    String getBusinessLicenseUrl(Long companyId);
}
