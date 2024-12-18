package cn.hjf.job.company.client;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.config.FeignRequestInterceptor;
import cn.hjf.job.model.vo.company.AddressInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-company", configuration = FeignRequestInterceptor.class)
public interface CompanyAddressFeignClient {

    /**
     * 获取公司地址
     *
     * @param addressId 获取地址
     * @return Result<AddressInfoVo>
     */
    @GetMapping("/address/{addressId}")
    Result<AddressInfoVo> getAddressById(@PathVariable(name = "addressId") Long addressId);
}
