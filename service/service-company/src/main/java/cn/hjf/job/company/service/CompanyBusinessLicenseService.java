package cn.hjf.job.company.service;

import cn.hjf.job.model.entity.company.CompanyBusinessLicense;
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

}
