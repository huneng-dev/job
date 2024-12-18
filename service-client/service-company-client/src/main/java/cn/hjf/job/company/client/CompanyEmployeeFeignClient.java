package cn.hjf.job.company.client;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.config.FeignRequestInterceptor;
import cn.hjf.job.model.vo.company.CompanyEmployeeVo;
import cn.hjf.job.model.vo.company.CompanyIdAndIsAdmin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@FeignClient(value = "service-company", configuration = FeignRequestInterceptor.class)
public interface CompanyEmployeeFeignClient {

    /**
     * 获取当前用户的公司 id
     *
     * @return 公司 id
     */
    @GetMapping("/employee/company/id")
    public Result<Long> findCompanyIdByUserId();

    @GetMapping("/employee/companyId-IsAdmin")
    public Result<CompanyIdAndIsAdmin> findCompanyIdAndIsAdminByUserId();

    /**
     * 获取员工信息 (同公司)
     *
     * @param targetId 目标用户 id
     * @return Result<CompanyEmployeeVo>
     */
    @GetMapping("/employee/{targetId}")
    public Result<CompanyEmployeeVo> findCompanyEmployeeById(@PathVariable(name = "targetId") Long targetId);
}
