package cn.hjf.job.resume.service.impl;

import cn.hjf.job.model.entity.resume.JobExpectation;
import cn.hjf.job.model.vo.resume.JobExpectationVo;
import cn.hjf.job.resume.mapper.JobExpectationMapper;
import cn.hjf.job.resume.service.JobExpectationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2025-01-08
 */
@Service
public class JobExpectationServiceImpl extends ServiceImpl<JobExpectationMapper, JobExpectation> implements JobExpectationService {

    @Resource
    private JobExpectationMapper jobExpectationMapper;

    @Override
    public JobExpectationVo getJobExpectationVo(Long resumeId) {
        LambdaQueryWrapper<JobExpectation> jobExpectationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jobExpectationLambdaQueryWrapper.eq(JobExpectation::getResumeId, resumeId);
        JobExpectation jobExpectation = jobExpectationMapper.selectOne(jobExpectationLambdaQueryWrapper);
        if (jobExpectation == null) {
            throw new RuntimeException();
        }

        JobExpectationVo jobExpectationVo = new JobExpectationVo();
        BeanUtils.copyProperties(jobExpectation, jobExpectationVo);
        return jobExpectationVo;
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<JobExpectationVo> getJobExpectationVoAsync(Long resumeId) {
        return CompletableFuture.supplyAsync(() -> getJobExpectationVo(resumeId));
    }
}
