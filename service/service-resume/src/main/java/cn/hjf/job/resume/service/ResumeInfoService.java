package cn.hjf.job.resume.service;

import cn.hjf.job.model.dto.resume.ResumeInfoDto;
import cn.hjf.job.model.entity.resume.ProjectExperience;
import cn.hjf.job.model.entity.resume.ResumeInfo;
import cn.hjf.job.model.form.resume.BaseResumeForm;
import cn.hjf.job.model.request.resume.ResumeSearchPageParam;
import cn.hjf.job.model.vo.base.PageEsVo;
import cn.hjf.job.model.vo.resume.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2025-01-07
 */
public interface ResumeInfoService extends IService<ResumeInfo> {

    /**
     * 创建基础简历
     *
     * @param baseResumeForm 基础简历表单
     * @param userId         用户 id
     * @return 简历 id
     */
    Long createBaseResume(BaseResumeForm baseResumeForm, Long userId);

    /**
     * 查询用户全部的基本简历
     *
     * @param userId 用户 id
     * @return List<BaseResumeVo>
     */
    List<BaseResumeVo> findBaseResumeList(Long userId);

    /**
     * 根据简历 id 查询简历详情
     *
     * @param resumeId 简历 id
     * @param userId   用户 id
     * @return ResumeInfoDto
     */
    ResumeInfoDto getResumeInfoById(Long resumeId, Long userId);

    /**
     * 更新 resumeInfo
     *
     * @param resumeVo 简历
     * @param userId   用户 id
     * @return 主键 id
     */
    Long updateResumeInfo(ResumeVo resumeVo, Long userId);

    Long updateEducationBackground(EducationBackgroundVo educationBackgroundVo, Long userId);

    Long updateJobExpectation(JobExpectationVo jobExpectationVo, Long userId);

    Long addProjectExperience(ProjectExperienceVo projectExperienceVo, Long userId);

    Boolean deleteProjectExperience(Long resumeId, @NotNull Long projectId, Long userId);

    Long addWorkExperience(WorkExperienceVo workExperienceVo, Long userId);

    Boolean deleteWorkExperience(Long resumeId, @NotNull Long workId, Long userId);

    Long addHonorAward(HonorAwardVo honorAwardVo, Long userId);

    Boolean deleteHonorAward(Long resumeId, @NotNull Long honorId, Long userId);

    Long addCertification(CertificationVo certificationVo, Long userId);

    Boolean deleteCertification(Long resumeId, Long certificationId, Long userId);

    /**
     * 获取 resumeVo
     * 同步
     *
     * @param resumeId 简历 id
     * @return ResumeVo
     */
    ResumeVo getResumeVo(Long resumeId);

    /**
     * 获取 resumeVo
     * 异步
     *
     * @param resumeId 简历 id
     * @return CompletableFuture<ResumeVo>
     */
    CompletableFuture<ResumeVo> getResumeVoAsync(Long resumeId);

    void testResumeSaveToES(Long resumeId);

    /**
     * 删除简历
     *
     * @param resumeId 简历 id
     * @param userId   用户 id
     * @return 是否成功
     */
    Boolean deleteResumeInfo(Long resumeId, Long userId);

    Boolean setResumeDefaultDisplay(Long resumeId, Long userId);

    PageEsVo<ResumeVoEs> searchBaseResumeInfoPage(Integer limit, ResumeSearchPageParam resumeSearchPageParam);

    Boolean isResumeExist(Long resumeId);

}



















