package cn.hjf.job.company.mapper;

import cn.hjf.job.model.entity.company.CompanyIndustry;
import cn.hjf.job.model.vo.company.IndustryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Mapper接口
 * @author hjf
 * @date 2024-10-31
 */
@Mapper
public interface CompanyIndustryMapper extends BaseMapper<CompanyIndustry> {

    public IPage<IndustryVo> selectAllIndustries(Page<IndustryVo> page);

}
