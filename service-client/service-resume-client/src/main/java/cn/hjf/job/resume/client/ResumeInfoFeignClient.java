package cn.hjf.job.resume.client;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.vo.interview.ResumeDeliveryVo;
import cn.hjf.job.model.vo.resume.ResumeVo;
import cn.hjf.job.resume.config.ResumeFeignRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@FeignClient(value = "service-resume", configuration = ResumeFeignRequestInterceptor.class)
public interface ResumeInfoFeignClient {


    /**
     * 招聘端获取用户的默认简历（基本信息）
     *
     * @param resumeId 简历 id
     * @return Result<ResumeVo>
     */
    @GetMapping("/resumeInfo/base/{resumeId}")
    Result<ResumeVo> getBaseResumeById(@PathVariable Long resumeId);

    /**
     * 应聘端获取用户的默认简历（基本信息）
     *
     * @return Result<ResumeVo>
     */
    @GetMapping("/resumeInfo/base/user")
    Result<ResumeVo> getBaseResume();

}
