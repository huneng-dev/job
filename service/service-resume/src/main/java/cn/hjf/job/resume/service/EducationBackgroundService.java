package cn.hjf.job.resume.service;

import cn.hjf.job.model.entity.resume.EducationBackground;
import cn.hjf.job.model.vo.resume.EducationBackgroundVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2025-01-07
 */
public interface EducationBackgroundService extends IService<EducationBackground> {

    EducationBackgroundVo getEducationBackgroundVo(Long resumeId);

    CompletableFuture<EducationBackgroundVo> getEducationBackgroundVoAsync(Long resumeId);

}
