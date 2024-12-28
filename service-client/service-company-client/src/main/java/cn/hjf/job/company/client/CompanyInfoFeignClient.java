package cn.hjf.job.company.client;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.config.FeignRequestInterceptor;
import cn.hjf.job.model.vo.company.CompanyInfoEsVo;
import cn.hjf.job.model.vo.company.CompanyInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-company", configuration = FeignRequestInterceptor.class)
public interface CompanyInfoFeignClient {

    /**
     * 获取公司信息 (ES 存储)
     *
     * @param id 公司 id
     * @return Result<CompanyInfoEsVo>
     */
    @GetMapping("/company/es/{id}")
    public Result<CompanyInfoEsVo> getCompanyInfoEsById(@PathVariable(name = "id") Long id);


    /**
     * 获取公司基本信息
     *
     * @param id 公司 id
     * @return Result<CompanyInfoVo>
     */
    @GetMapping("/company/base/{id}")
    public Result<CompanyInfoVo> getCompanyInfoVo(@PathVariable(name = "id") Long id);
}
