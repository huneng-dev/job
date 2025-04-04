package cn.hjf.job.company.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.common.result.ResultCodeEnum;
import cn.hjf.job.company.service.CompanyInfoService;
import cn.hjf.job.company.service.CompanySizeService;
import cn.hjf.job.model.dto.company.CompanyIdAndNameDTO;
import cn.hjf.job.model.dto.company.CompanyInfoQuery;
import cn.hjf.job.model.form.company.CompanyInfoAndBusinessLicenseForm;
import cn.hjf.job.model.vo.company.*;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @Resource(name = "companyInfoServiceImpl")
    private CompanyInfoService companyInfoService;

    @Resource(name = "companySizeServiceImpl")
    private CompanySizeService companySizeService;


    /**
     * 获取当前用户的公司信息
     *
     * @param principal 用户信息
     * @return Result<CompanyInfoQuery>
     */
    @GetMapping("/base/info")
    @PreAuthorize("hasRole('ROLE_BASE_RECRUITER')")
    public Result<CompanyInfoQuery> getBaseCompanyInfo(Principal principal) {
        CompanyInfoQuery companyInfoQuery = companyInfoService.findCompanyInfoByUserId(Long.parseLong(principal.getName()));
        if (companyInfoQuery == null) {
            // 返回用户不存在
            return Result.build(null, ResultCodeEnum.EMPLOYEE_COMPANY_NOT_FOUND);
        }

        return Result.ok(companyInfoQuery);
    }

    /**
     * 获取公司名列表
     *
     * @param name 公司名
     * @return Result<List < CompanyIdAndNameDTO>>
     */
    @GetMapping("/index/{name}")
    public Result<List<CompanyIdAndNameDTO>> getCompanyIndexAndNameByName(@PathVariable(name = "name") String name) {
        if (name == null || name.length() < 2) {
            return Result.fail();
        }
        return Result.ok(companyInfoService.findCompanyIndexAndNameByName(name));
    }

    /**
     * 获取全部公司范围
     *
     * @return Result<List < CompanySizeVo>>
     */
    @GetMapping("/employee/size/all")
    public Result<List<CompanySizeVo>> getCompanySizeList() {
        List<CompanySizeVo> companySizeAll = companySizeService.findCompanySizeAll();
        return Result.ok(companySizeAll);
    }

    /**
     * 保存公司信息和营业执照信息(注册公司流程)
     *
     * @param companyInfoAndBusinessLicenseForm 公司信息和营业执照表单
     * @return Result<String>
     */
    @PostMapping("/info")
    @PreAuthorize("hasRole('ROLE_BASE_RECRUITER')")
    public Result<String> saveCompanyInfoAndBusinessLicense(
            @RequestBody CompanyInfoAndBusinessLicenseForm companyInfoAndBusinessLicenseForm,
            Principal principal
    ) {
        try {
            companyInfoService.saveCompanyInfoAndBusinessLicense(companyInfoAndBusinessLicenseForm, Long.parseLong(principal.getName()));
            return Result.ok("保存成功: 等待审核");
        } catch (RuntimeException e) {
            return Result.fail("保存失败: " + e.getCause().getMessage());
        }
    }

    /**
     * 获取公司信息 (ES 存储)
     *
     * @param id 公司 id
     * @return Result<CompanyInfoEsVo>
     */
    @GetMapping("/es/{id}")
    public Result<CompanyInfoEsVo> getCompanyInfoEsById(@PathVariable(name = "id") Long id) {
        CompanyInfoEsVo companyInfoEsById = companyInfoService.getCompanyInfoEsById(id);
        return Result.ok(companyInfoEsById);
    }

    /**
     * 获取公司基本信息
     *
     * @param id 公司 id
     * @return Result<CompanyInfoVo>
     */
    @GetMapping("/base/{id}")
    public Result<CompanyInfoVo> getCompanyInfoVo(@PathVariable(name = "id") Long id) {
        try {
            CompanyInfoVo companyInfoById = companyInfoService.getCompanyInfoById(id);
            return Result.ok(companyInfoById);
        } catch (Exception e) {
            return Result.fail();
        }
    }


    /**
     * 招聘端获取公司详细
     *
     * @param principal 用户信息
     * @return Result<CompanyInfoRecruiterVo>
     */
    @GetMapping("/detail/recruiter")
    @PreAuthorize("hasRole('ROLE_ADMIN_RECRUITER')")
    public Result<CompanyInfoRecruiterVo> getCompanyInfoRecruiterVo(Principal principal) {
        try {
            CompanyInfoRecruiterVo companyInfoRecruiterVo = companyInfoService.getCompanyInfoRecruiterVo(Long.parseLong(principal.getName()));
            return companyInfoRecruiterVo != null ? Result.ok(companyInfoRecruiterVo) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 获取公司详情 (公共)
     *
     * @param companyId 公司id
     * @return Result<CompanyInfoCandidateVo>
     */
    @GetMapping("/detail/candidate")
    public Result<CompanyInfoCandidateVo> getCompanyInfoCandidateVo(@RequestParam Long companyId) {
        try {
            CompanyInfoCandidateVo companyInfoCandidateVo = companyInfoService.getCompanyInfoCandidateVo(companyId);
            return companyInfoCandidateVo != null ? Result.ok(companyInfoCandidateVo) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }


    /**
     * 获取公司行业 id
     *
     * @return 行业 id
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_RECRUITER')")
    @GetMapping("/companyIndustryId")
    public Result<Long> getCompanyIndustryIdById(Principal principal) {
        Long companyIndustryId = companyInfoService.getCompanyIndustryId(Long.parseLong(principal.getName()));
        return Result.ok(companyIndustryId);
    }
}
