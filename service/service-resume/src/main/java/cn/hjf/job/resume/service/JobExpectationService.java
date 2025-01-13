package cn.hjf.job.resume.service;

import cn.hjf.job.model.entity.resume.JobExpectation;
import cn.hjf.job.model.vo.resume.JobExpectationVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2025-01-08
 */
public interface JobExpectationService extends IService<JobExpectation> {

    /**
     * 获取工作期望
     * 同步
     *
     * @param resumeId 简历 id
     * @return JobExpectationVo
     */
    JobExpectationVo getJobExpectationVo(Long resumeId);

    CompletableFuture<JobExpectationVo> getJobExpectationVoAsync(Long resumeId);
}
