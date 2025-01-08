package cn.hjf.job.resume.service.impl;

import cn.hjf.job.model.dto.resume.ResumeInfoDto;
import cn.hjf.job.model.entity.resume.*;
import cn.hjf.job.model.form.resume.BaseResumeForm;
import cn.hjf.job.model.vo.resume.*;
import cn.hjf.job.resume.mapper.*;
import cn.hjf.job.resume.service.ResumeInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2025-01-07
 */
@Service
public class ResumeInfoServiceImpl extends ServiceImpl<ResumeInfoMapper, ResumeInfo> implements ResumeInfoService {

    @Resource
    private ResumeInfoMapper resumeInfoMapper;

    @Resource
    private EducationBackgroundMapper educationBackgroundMapper;

    @Resource
    private JobExpectationMapper jobExpectationMapper;

    @Resource
    private ProjectExperienceMapper projectExperienceMapper;

    @Resource
    private WorkExperienceMapper workExperienceMapper;

    @Resource
    private HonorAwardMapper honorAwardMapper;

    @Resource
    private CertificationMapper certificationMapper;


    @Override
    @GlobalTransactional(name = "create-resume", rollbackFor = Exception.class)
    public Long createBaseResume(BaseResumeForm baseResumeForm, Long userId) {

        // 每人只能有10个简历(超出不能在创建)
        LambdaQueryWrapper<ResumeInfo> selectResumeCountQueryWrapper = new LambdaQueryWrapper<>();
        selectResumeCountQueryWrapper.eq(ResumeInfo::getCandidateId, userId);
        Long resumeCount = resumeInfoMapper.selectCount(selectResumeCountQueryWrapper);
        if (resumeCount >= 10) return null;

        // 保存简历基本信息
        ResumeInfo resumeInfo = new ResumeInfo();
        BeanUtils.copyProperties(baseResumeForm.getResumeInfoForm(), resumeInfo);
        resumeInfo.setCandidateId(userId);
        resumeInfo.setId(null); // 创建简历 id必须为 null
        if (resumeCount == 0) { //表示 当前简历是用户一个简历 （设置为默认展示）
            resumeInfo.setIsDefaultDisplay(1);
        }

        // 保存
        int resumeInsert = resumeInfoMapper.insert(resumeInfo);

        if (resumeInsert != 1 || resumeInfo.getId() == null) {
            throw new RuntimeException("保存简历基本信息失败");
        }

        // 保存工作期望
        JobExpectation jobExpectation = new JobExpectation();
        BeanUtils.copyProperties(baseResumeForm.getJobExpectationForm(), jobExpectation);
        jobExpectation.setResumeId(resumeInfo.getId());
        jobExpectation.setId(null);

        if (baseResumeForm.getJobExpectationForm().getSalaryMin() == 0) {
            jobExpectation.setIsNegotiable(1);
        }

        int jobInsert = jobExpectationMapper.insert(jobExpectation);
        if (jobInsert != 1) {
            throw new RuntimeException("工作期望");
        }

        // 保存教育背景
        EducationBackground educationBackground = new EducationBackground();
        BeanUtils.copyProperties(baseResumeForm.getEducationBackgroundForm(), educationBackground);
        educationBackground.setResumeId(resumeInfo.getId());
        educationBackground.setId(null);

        int eduInsert = educationBackgroundMapper.insert(educationBackground);
        if (eduInsert != 1) {
            throw new RuntimeException("教育背景");
        }

        // 如果不是第一次创建,且用户设置当前简历是默认,设置除了当前 简历以外的全部简历为 非默认
        if (resumeCount > 0 && Objects.equals(baseResumeForm.getResumeInfoForm().getIsDefaultDisplay(), 1)) {
            LambdaUpdateWrapper<ResumeInfo> resumeInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            resumeInfoLambdaUpdateWrapper.set(ResumeInfo::getIsDefaultDisplay, 0).eq(ResumeInfo::getCandidateId, userId).ne(ResumeInfo::getId, resumeInfo.getId());

            resumeInfoMapper.update(resumeInfoLambdaUpdateWrapper); // 如果出异常也会正常回滚
        }
        return resumeInfo.getId();
    }

    @Override
    public List<BaseResumeVo> findBaseResumeList(Long userId) {

        // 查询全部的简历
        LambdaQueryWrapper<ResumeInfo> resumeInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        resumeInfoLambdaQueryWrapper.eq(ResumeInfo::getCandidateId, userId);
        List<ResumeInfo> resumeInfos = resumeInfoMapper.selectList(resumeInfoLambdaQueryWrapper);

        List<BaseResumeVo> baseResumeVos = new ArrayList<>(resumeInfos.size());

        List<Long> resumeIds = new ArrayList<>(resumeInfos.size());

        baseResumeVos = resumeInfos.stream().map(resumeInfo -> {
            BaseResumeVo baseResumeVo = new BaseResumeVo();
            baseResumeVo.setId(resumeInfo.getId());
            resumeIds.add(resumeInfo.getId());
            baseResumeVo.setResumeName(resumeInfo.getResumeName());
            baseResumeVo.setIsDefaultDisplay(resumeInfo.getIsDefaultDisplay());
            return baseResumeVo;
        }).toList();

        // 查询全部的工作期望
        LambdaQueryWrapper<JobExpectation> jobExpectationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jobExpectationLambdaQueryWrapper.in(JobExpectation::getResumeId, resumeIds);
        List<JobExpectation> jobExpectations = jobExpectationMapper.selectList(jobExpectationLambdaQueryWrapper);

        LambdaQueryWrapper<ProjectExperience> projectExperienceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectExperienceLambdaQueryWrapper.select(ProjectExperience::getId, ProjectExperience::getResumeId, ProjectExperience::getProjectName, ProjectExperience::getRole).in(ProjectExperience::getResumeId, resumeIds);
        List<ProjectExperience> projectExperiences = projectExperienceMapper.selectList(projectExperienceLambdaQueryWrapper);

        for (int i = 0; i < baseResumeVos.size(); i++) {
            baseResumeVos.get(i).setWorkCity(jobExpectations.get(i).getWorkCity());
            baseResumeVos.get(i).setSalaryMin(jobExpectations.get(i).getSalaryMin());
            baseResumeVos.get(i).setSalaryMax(jobExpectations.get(i).getSalaryMax());
            baseResumeVos.get(i).setIsNegotiable(jobExpectations.get(i).getIsNegotiable());
            baseResumeVos.get(i).setJobType(jobExpectations.get(i).getJobType());

            // 如果有项目就进行匹配
            if (projectExperiences != null && !projectExperiences.isEmpty()) {
                List<BaseProjectExperienceVo> baseProjectExperienceVos = getBaseProjectExperienceVos(projectExperiences, baseResumeVos);
                baseResumeVos.get(i).setBaseProjectExperienceVoList(baseProjectExperienceVos);
            }
        }

        return baseResumeVos;
    }

    @Override
    public ResumeInfoDto getResumeInfoById(Long resumeId, Long userId) {

        ResumeInfo resumeInfo = resumeInfoMapper.selectById(resumeId);

        // 判断当前请去的简历是否属于当前用户

        if (!Objects.equals(userId, resumeInfo.getCandidateId())) {
            return null;
        }

        ResumeInfoDto resumeInfoDto = new ResumeInfoDto();

        // 获取resume信息
        ResumeVo resumeVo = new ResumeVo();
        BeanUtils.copyProperties(resumeInfo, resumeVo);
        resumeInfoDto.setResumeVo(resumeVo);

        // 获取教育背景
        LambdaQueryWrapper<EducationBackground> educationBackgroundLambdaQueryWrapper = new LambdaQueryWrapper<>();
        educationBackgroundLambdaQueryWrapper.eq(EducationBackground::getResumeId, resumeId);
        EducationBackground educationBackground = educationBackgroundMapper.selectOne(educationBackgroundLambdaQueryWrapper);

        EducationBackgroundVo educationBackgroundVo = new EducationBackgroundVo();
        BeanUtils.copyProperties(educationBackground, educationBackgroundVo);
        resumeInfoDto.setEducationBackgroundVo(educationBackgroundVo);

        // 获取工作期望
        LambdaQueryWrapper<JobExpectation> jobExpectationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jobExpectationLambdaQueryWrapper.eq(JobExpectation::getResumeId, resumeId);
        JobExpectation jobExpectation = jobExpectationMapper.selectOne(jobExpectationLambdaQueryWrapper);

        JobExpectationVo jobExpectationVo = new JobExpectationVo();
        BeanUtils.copyProperties(jobExpectation, jobExpectationVo);
        resumeInfoDto.setJobExpectationVo(jobExpectationVo);

        // 获取项目列表
        LambdaQueryWrapper<ProjectExperience> projectExperienceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectExperienceLambdaQueryWrapper.eq(ProjectExperience::getResumeId, resumeId);
        List<ProjectExperience> projectExperiences = projectExperienceMapper.selectList(projectExperienceLambdaQueryWrapper);

        List<ProjectExperienceVo> projectExperienceVos = projectExperiences.stream().map(projectExperience -> {
            ProjectExperienceVo projectExperienceVo = new ProjectExperienceVo();
            BeanUtils.copyProperties(projectExperience, projectExperienceVo);
            return projectExperienceVo;
        }).toList();

        resumeInfoDto.setProjectExperienceVos(projectExperienceVos);

        // 获取工作经历
        LambdaQueryWrapper<WorkExperience> workExperienceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        workExperienceLambdaQueryWrapper.eq(WorkExperience::getResumeId, resumeId);
        List<WorkExperience> workExperiences = workExperienceMapper.selectList(workExperienceLambdaQueryWrapper);

        List<WorkExperienceVo> workExperienceVos = workExperiences.stream().map(workExperience -> {
            WorkExperienceVo workExperienceVo = new WorkExperienceVo();
            BeanUtils.copyProperties(workExperience, workExperienceVo);
            return workExperienceVo;
        }).toList();

        resumeInfoDto.setWorkExperienceVos(workExperienceVos);

        // 获取奖励荣誉
        LambdaQueryWrapper<HonorAward> honorAwardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        honorAwardLambdaQueryWrapper.eq(HonorAward::getResumeId, resumeId);
        List<HonorAward> honorAwards = honorAwardMapper.selectList(honorAwardLambdaQueryWrapper);

        List<HonorAwardVo> honorAwardVos = honorAwards.stream().map(honorAward -> {
            HonorAwardVo honorAwardVo = new HonorAwardVo();
            BeanUtils.copyProperties(honorAward, honorAwardVo);
            return honorAwardVo;
        }).toList();

        resumeInfoDto.setHonorAwardVos(honorAwardVos);

        // 获取证书
        LambdaQueryWrapper<Certification> certificationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        certificationLambdaQueryWrapper.eq(Certification::getResumeId, resumeId);
        List<Certification> certifications = certificationMapper.selectList(certificationLambdaQueryWrapper);

        List<CertificationVo> certificationVos = certifications.stream().map(certification -> {
            CertificationVo certificationVo = new CertificationVo();
            BeanUtils.copyProperties(certification, certificationVo);
            return certificationVo;
        }).toList();

        resumeInfoDto.setCertificationVos(certificationVos);

        return resumeInfoDto;
    }

    @NotNull
    private static List<BaseProjectExperienceVo> getBaseProjectExperienceVos(List<ProjectExperience> projectExperiences, List<BaseResumeVo> baseResumeVos) {
        List<BaseProjectExperienceVo> baseProjectExperienceVos = new ArrayList<>();
        for (int x = 0; x < projectExperiences.size(); x++) {
            if (Objects.equals(projectExperiences.get(x).getResumeId(), baseResumeVos.get(x).getId())) {
                BaseProjectExperienceVo baseProjectExperienceVo = new BaseProjectExperienceVo(projectExperiences.get(x).getId(), projectExperiences.get(x).getProjectName(), projectExperiences.get(x).getRole());
                baseProjectExperienceVos.add(baseProjectExperienceVo);
            }
        }
        return baseProjectExperienceVos;
    }
}
















