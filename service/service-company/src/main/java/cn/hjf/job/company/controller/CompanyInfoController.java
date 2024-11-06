package cn.hjf.job.company.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.service.CompanyInfoService;
import cn.hjf.job.model.entity.company.CompanyInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyInfoController {

    @Resource
    private CompanyInfoService companyInfoService;

    @GetMapping("/{id}")
    public Result<CompanyInfo> getCompanyInfoById(@PathVariable(name = "id") Integer id) {
        return Result.ok(companyInfoService.getCompanyInfoById(id));
    }
}
