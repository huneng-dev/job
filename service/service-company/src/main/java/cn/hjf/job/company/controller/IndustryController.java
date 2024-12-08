package cn.hjf.job.company.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.service.CompanyIndustryService;
import cn.hjf.job.model.entity.company.CompanyIndustry;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.company.IndustryVo;
import cn.hjf.job.model.vo.company.ParentIndustryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 行业信息管理
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController
@RequestMapping("/industry")
public class IndustryController {

    @Resource
    private CompanyIndustryService companyIndustryService;


    /**
     * 获取行业page
     *
     * @param page  页
     * @param limit 没页多少
     * @return Result<PageVo < IndustryVo>>
     */
    @GetMapping("/findIndustryPage/{page}/{limit}")
    public Result<PageVo<IndustryVo>> findIndustryPage(
            @PathVariable(name = "page") Long page,
            @PathVariable(name = "limit") Long limit
    ) {
        Page<CompanyIndustry> industryPage = new Page<>(page, limit);
        PageVo<IndustryVo> industryVoPageVo = companyIndustryService.selectIndustryByPage(industryPage);
        industryVoPageVo.setPage(page);
        industryVoPageVo.setLimit(limit);
        return Result.ok(industryVoPageVo);
    }

    /**
     * 获取全部父行业
     *
     * @return Result<List < ParentIndustryVo>>
     */
    @GetMapping("/parent/all")
    public Result<List<ParentIndustryVo>> findParentIndustryAll() {
        List<ParentIndustryVo> parentIndustryAll = companyIndustryService.getParentIndustryAll();
        return Result.ok(parentIndustryAll);
    }
}
