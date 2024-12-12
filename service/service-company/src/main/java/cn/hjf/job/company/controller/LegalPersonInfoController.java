package cn.hjf.job.company.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.service.CompanyBusinessLicenseService;
import cn.hjf.job.company.service.CompanyEmployeeService;
import cn.hjf.job.company.service.CompanyInfoService;
import cn.hjf.job.company.service.LegalPersonInfoService;
import cn.hjf.job.model.dto.company.CompanyInfoQuery;
import cn.hjf.job.model.entity.company.CompanyBusinessLicense;
import cn.hjf.job.model.vo.company.LegalPersonInfoVo;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 企业法人控制器
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController
@RequestMapping("/legal/person")
public class LegalPersonInfoController {

    @Resource(name = "legalPersonInfoServiceImpl")
    private LegalPersonInfoService legalPersonInfoService;

    @Resource(name = "companyInfoServiceImpl")
    private CompanyInfoService companyInfoService;

    @Resource(name = "companyEmployeeServiceImpl")
    private CompanyEmployeeService companyEmployeeService;

    @Resource(name = "companyBusinessLicenseServiceImpl")
    private CompanyBusinessLicenseService companyBusinessLicenseService;

    /**
     * 保存企业法人信息
     *
     * @param legalPersonInfoVo 企业法人信息
     * @return Result<String>
     */
    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_BASE_RECRUITER')")
    public Result<String> saveLegalPersonInfo(@RequestBody LegalPersonInfoVo legalPersonInfoVo, Principal principal) {
        // 获取用户 id
        long userId = Long.parseLong(principal.getName());
        // 获取 companyId
        Long companyId = companyEmployeeService.findCompanyIdByUserId(userId);
        if (companyId == null) {
            return Result.fail();
        }

        // 获取营业执照 id
        Long businessLicenseId = companyBusinessLicenseService.findBusinessLicenseIdByCompanyId(companyId);
        if (businessLicenseId == null) {
            return Result.fail();
        }
        try {
            boolean isSuccess = legalPersonInfoService.saveLegalPersonInfo(legalPersonInfoVo, userId, companyId, businessLicenseId);
            if (!isSuccess) {
                return Result.fail();
            }
        } catch (Exception e) {
            return Result.fail();
        }

        return Result.ok();
    }
}
