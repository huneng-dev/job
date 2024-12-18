package cn.hjf.job.company.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.service.CompanyEmployeeService;
import cn.hjf.job.model.entity.company.CompanyEmployee;
import cn.hjf.job.model.request.company.AddEmployeeToCompanyRequest;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.company.CompanyEmployeeVo;
import cn.hjf.job.model.vo.company.CompanyIdAndIsAdmin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
            boolean isSuccess = companyEmployeeService.addEmployeeToCompany(Long.parseLong(principal.getName()), addEmployeeToCompanyRequest.getCompanyId(), addEmployeeToCompanyRequest.getVerificationCode());
            if (!isSuccess) {
                return Result.fail("无法加入");
            }
            return Result.ok("加入成功");
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取公司员工分页
     *
     * @param page      第几页
     * @param limit     每页多少
     * @param principal 用户数据
     * @return Result<PageVo < CompanyEmployeeVo>>
     */
    @GetMapping("/{page}/{limit}")
    public Result<PageVo<CompanyEmployeeVo>> findCompanyEmployeePage(@PathVariable(name = "page") Long page, @PathVariable(name = "limit") Long limit, Principal principal) {
        Page<CompanyEmployee> companyEmployeePage = new Page<>(page, limit);

        PageVo<CompanyEmployeeVo> companyEmployeePageVo = companyEmployeeService.findCompanyEmployeePage(companyEmployeePage, Long.parseLong(principal.getName()));

        return Result.ok(companyEmployeePageVo);
    }

    /**
     * 获取员工信息 (同公司)
     *
     * @param targetId  目标用户 id
     * @param principal 用户
     * @return Result<CompanyEmployeeVo>
     */
    @GetMapping("/{targetId}")
    public Result<CompanyEmployeeVo> findCompanyEmployeeById(@PathVariable(name = "targetId") Long targetId, Principal principal) {
        try {
            CompanyEmployeeVo companyEmployeeVo = companyEmployeeService.findCompanyEmployeeById(targetId, Long.parseLong(principal.getName()));
            return Result.ok(companyEmployeeVo);
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 获取当前用户的公司 id
     *
     * @param principal 用户信息
     * @return 公司 id
     */
    @PreAuthorize("hasRole('ROLE_ADMIN_RECRUITER')")
    @GetMapping("/company/id")
    public Result<Long> findCompanyIdByUserId(Principal principal) {
        Long companyId = companyEmployeeService.findCompanyIdByUserId(Long.parseLong(principal.getName()));
        return Result.ok(companyId);
    }

    @GetMapping("/companyId-IsAdmin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_RECRUITER','ROLE_EMPLOYEE_RECRUITER')")
    public Result<CompanyIdAndIsAdmin> findCompanyIdAndIsAdminByUserId(Principal principal) {
        CompanyIdAndIsAdmin companyIdAndIsAdmin = companyEmployeeService.findCompanyIdAndIsAdminByUserId(Long.parseLong(principal.getName()));
        return Result.ok(companyIdAndIsAdmin);
    }
}
