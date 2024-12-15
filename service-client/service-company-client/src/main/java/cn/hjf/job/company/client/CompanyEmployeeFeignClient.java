package cn.hjf.job.company.client;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.config.FeignRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

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
}
