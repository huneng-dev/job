package cn.hjf.job.resume.service.impl;

import cn.hjf.job.model.entity.resume.EducationBackground;
import cn.hjf.job.model.vo.resume.EducationBackgroundVo;
import cn.hjf.job.resume.mapper.EducationBackgroundMapper;
import cn.hjf.job.resume.service.EducationBackgroundService;
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
 * @since 2025-01-07
 */
@Service
public class EducationBackgroundServiceImpl extends ServiceImpl<EducationBackgroundMapper, EducationBackground> implements EducationBackgroundService {

    @Resource
    private EducationBackgroundMapper educationBackgroundMapper;

    @Override
    public EducationBackgroundVo getEducationBackgroundVo(Long resumeId) {
        LambdaQueryWrapper<EducationBackground> educationBackgroundLambdaQueryWrapper = new LambdaQueryWrapper<>();
        educationBackgroundLambdaQueryWrapper.eq(EducationBackground::getResumeId, resumeId);
        EducationBackground educationBackground = educationBackgroundMapper.selectOne(educationBackgroundLambdaQueryWrapper);
        if (educationBackground == null) throw new RuntimeException();
        EducationBackgroundVo educationBackgroundVo = new EducationBackgroundVo();
        BeanUtils.copyProperties(educationBackground, educationBackgroundVo);
        return educationBackgroundVo;
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<EducationBackgroundVo> getEducationBackgroundVoAsync(Long resumeId) {
        return CompletableFuture.supplyAsync(() -> getEducationBackgroundVo(resumeId));
    }
}
