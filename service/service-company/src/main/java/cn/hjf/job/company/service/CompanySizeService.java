package cn.hjf.job.company.service;

import cn.hjf.job.model.entity.company.CompanySize;
import cn.hjf.job.model.vo.company.CompanySizeVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
public interface CompanySizeService extends IService<CompanySize> {

    /**
     * 获取全部公司范围选项
     *
     * @return List<CompanySizeVo>
     */
    List<CompanySizeVo> findCompanySizeAll();

}
