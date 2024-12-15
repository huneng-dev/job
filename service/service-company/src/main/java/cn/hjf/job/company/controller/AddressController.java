package cn.hjf.job.company.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.service.CompanyAddressService;
import cn.hjf.job.model.entity.company.CompanyAddress;
import cn.hjf.job.model.form.company.AddressInfoForm;
import cn.hjf.job.model.vo.company.AddressInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * 地址控制器
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController
@RequestMapping("/address")
public class AddressController {

    @Resource(name = "companyAddressServiceImpl")
    private CompanyAddressService companyAddressService;

    /**
     * 添加公司地址
     *
     * @param addressInfoForm 地址表单
     * @param principal       用户自己
     * @return Result<String>
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN_RECRUITER')")
    public Result<String> add(@RequestBody AddressInfoForm addressInfoForm, Principal principal) {
        try {
            boolean isSuccess = companyAddressService.add(addressInfoForm, Long.parseLong(principal.getName()));

            return isSuccess ? Result.ok("成功") : Result.fail("失败");
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取当前用户公司的地址
     *
     * @param principal 用户信息
     * @return Result<List < AddressInfoVo>>
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_RECRUITER')")
    public Result<List<AddressInfoVo>> findAllCompanyAddressByUserId(Principal principal) {
        try {
            List<AddressInfoVo> allCompanyAddress = companyAddressService.findAllCompanyAddressByUserId(Long.parseLong(principal.getName()));
            return Result.ok(allCompanyAddress);
        } catch (Exception e) {
            return Result.fail();
        }
    }

}
