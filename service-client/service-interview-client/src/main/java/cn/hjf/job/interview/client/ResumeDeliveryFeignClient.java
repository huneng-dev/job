package cn.hjf.job.interview.client;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.interview.config.InterviewFeignRequestInterceptor;
import cn.hjf.job.model.vo.interview.ResumeDeliveryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "service-interview", configuration = InterviewFeignRequestInterceptor.class)
public interface ResumeDeliveryFeignClient {

    /**
     * 投递者获取简历投递投递记录
     *
     * @param resumeId 简历id
     * @return 简历投递记录
     */
    @GetMapping("/resumeDelivery/resume/{resumeId}/{positionId}")
    Result<ResumeDeliveryVo> getResumeDeliveryVoByResumeId(@PathVariable(name = "resumeId") Long resumeId, @PathVariable(name = "positionId") Long positionId);
}
