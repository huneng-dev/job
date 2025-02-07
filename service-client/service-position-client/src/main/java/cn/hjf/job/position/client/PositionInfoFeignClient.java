package cn.hjf.job.position.client;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.vo.position.CandidateBasePositionInfoVo;
import cn.hjf.job.position.config.PositionFeignRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-position", configuration = PositionFeignRequestInterceptor.class)
public interface PositionInfoFeignClient {

    @GetMapping("/positionInfo/public/base/{positionId}")
    Result<CandidateBasePositionInfoVo> getPublicBasePositionInfoById(@PathVariable(name = "positionId") Long positionId);
}
