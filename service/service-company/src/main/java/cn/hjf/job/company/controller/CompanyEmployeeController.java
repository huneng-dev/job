package cn.hjf.job.company.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.service.CompanyEmployeeService;
import cn.hjf.job.model.request.company.AddEmployeeToCompanyRequest;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * 人员管理
 *
 * @author hjf
 * @version 1.0
 * @description
 */
@RestController
@RequestMapping("/employee")
public class CompanyEmployeeController {

    @Resource(name = "companyEmployeeServiceImpl")
    private CompanyEmployeeService companyEmployeeService;

    /**
     * 获取加入公司的验证码
     *
     * @param principal 用户身份
     * @return 验证码
     */
    @GetMapping("/verification/code")
    @PreAuthorize("hasRole('ROLE_ADMIN_RECRUITER')")
    public Result<String> getAddEmployeeVerificationCode(Principal principal) {

        String verificationCode = companyEmployeeService.getVerificationCode(Long.parseLong(principal.getName()));
        if (verificationCode == null) return Result.fail();

        return Result.ok(verificationCode);
    }

    /**
     * 加入公司
     *
     * @param addEmployeeToCompanyRequest 公司与验证码
     * @param principal                   当前用户
     * @return 是否成功
     */
    @PreAuthorize("hasRole('ROLE_BASE_RECRUITER')")
    @PostMapping("/add/company")
    public Result<String> addEmployeeToCompany(@RequestBody AddEmployeeToCompanyRequest addEmployeeToCompanyRequest, Principal principal) {

        try {
            boolean isSuccess = companyEmployeeService.addEmployeeToCompany(
                    Long.parseLong(principal.getName()),
                    addEmployeeToCompanyRequest.getCompanyId(),
                    addEmployeeToCompanyRequest.getVerificationCode()
            );
            if (!isSuccess) {
                return Result.fail("无法加入");
            }
            return Result.ok("加入成功");
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

}
