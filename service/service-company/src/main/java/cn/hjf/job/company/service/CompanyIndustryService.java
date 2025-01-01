package cn.hjf.job.company.service;

import cn.hjf.job.model.entity.company.CompanyIndustry;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.company.IndustryVo;
import cn.hjf.job.model.vo.company.ParentIndustryVo;
import cn.hjf.job.model.vo.company.SubIndustriesVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
public interface CompanyIndustryService extends IService<CompanyIndustry> {

    public PageVo<IndustryVo> selectIndustryByPage(Page<CompanyIndustry> couponInfoPage);

    public List<SubIndustriesVo> getSubIndustriesFromRedisOrDb(Long parentId);

    /**
     * 获取全部的父行业
     *
     * @return List<ParentIndustryVo>
     */
    public List<ParentIndustryVo> getParentIndustryAll();

    /**
     * 通过id获取行业描述
     *
     * @param id 行业 id
     * @return String
     */
    String getIndustryDesc(Long id);
}
