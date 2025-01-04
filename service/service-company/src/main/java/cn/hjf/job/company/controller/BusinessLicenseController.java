package cn.hjf.job.company.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.service.CompanyBusinessLicenseService;
import cn.hjf.job.company.service.CompanyEmployeeService;
import cn.hjf.job.model.vo.company.CompanyBusinessLicenseCandidate;
import cn.hjf.job.model.vo.company.CompanyBusinessLicenseRecruiterVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 营业执照控制器
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController()
@RequestMapping("/business-license")
public class BusinessLicenseController {

    @Resource(name = "companyEmployeeServiceImpl")
    private CompanyEmployeeService companyEmployeeService;

    @Resource(name = "companyBusinessLicenseServiceImpl")
    private CompanyBusinessLicenseService companyBusinessLicenseService;

    /**
     * 招聘端管理员获取营业执照
     *
     * @param principal 用户信息
     * @return Result<CompanyBusinessLicenseRecruiterVo>
     */
    @GetMapping("/detail/recruiter")
    @PreAuthorize("hasRole('ROLE_ADMIN_RECRUITER')")
    public Result<CompanyBusinessLicenseRecruiterVo> getCompanyBusinessLicenseRecruiterVo(Principal principal) {
        try {
            Long companyId = companyEmployeeService.findCompanyIdByUserId(Long.parseLong(principal.getName()));
            if (companyId == null) return Result.fail();
            CompanyBusinessLicenseRecruiterVo companyBusinessLicenseRecruiterVo = companyBusinessLicenseService.getCompanyBusinessLicenseRecruiterVo(companyId);
            return Result.ok(companyBusinessLicenseRecruiterVo);
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 招聘端管理员获取营业执照 url
     *
     * @param principal 用户信息
     * @return Result<String>
     */
    @GetMapping("/url/recruiter")
    @PreAuthorize("hasRole('ROLE_ADMIN_RECRUITER')")
    public Result<String> getBusinessLicenseUrl(Principal principal) {
        try {
            Long companyId = companyEmployeeService.findCompanyIdByUserId(Long.parseLong(principal.getName()));
            if (companyId == null) return Result.fail();
            String businessLicenseUrl = companyBusinessLicenseService.getBusinessLicenseUrl(companyId);
            return businessLicenseUrl != null ? Result.ok(businessLicenseUrl) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 应聘端获取营业执照
     *
     * @param companyId 公司 id
     * @return Result<CompanyBusinessLicenseCandidate>
     */
    @GetMapping("/detail/candidate")
    public Result<CompanyBusinessLicenseCandidate> getCompanyBusinessLicenseCandidate(Long companyId) {
        CompanyBusinessLicenseRecruiterVo companyBusinessLicenseRecruiterVo = companyBusinessLicenseService.getCompanyBusinessLicenseRecruiterVo(companyId);
        CompanyBusinessLicenseCandidate companyBusinessLicenseCandidate = new CompanyBusinessLicenseCandidate();
        BeanUtils.copyProperties(companyBusinessLicenseRecruiterVo, companyBusinessLicenseCandidate);
        companyBusinessLicenseCandidate.setId(null);
        return Result.ok(companyBusinessLicenseCandidate);
    }
}
