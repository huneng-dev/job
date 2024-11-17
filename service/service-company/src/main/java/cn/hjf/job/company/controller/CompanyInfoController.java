package cn.hjf.job.company.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.service.CompanyInfoService;
import cn.hjf.job.model.entity.company.CompanyInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公司信息管理
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController
@RequestMapping("/company")
public class CompanyInfoController {

    @Resource
    private CompanyInfoService companyInfoService;

    /**
     * 获取公司信息
     *
     * @param id 公司id
     * @return 公司信息
     */
    @GetMapping("/{id}")
    public Result<CompanyInfo> getCompanyInfoById(@PathVariable(name = "id") Integer id) {
        return Result.ok(companyInfoService.getCompanyInfoById(id));
    }
}
