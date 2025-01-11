package cn.hjf.job.resume.service.impl;

import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.rabbit.service.RabbitService;
import cn.hjf.job.model.document.resume.ProjectDescriptionDoc;
import cn.hjf.job.model.document.resume.WorkDescriptionDoc;
import cn.hjf.job.model.dto.resume.ResumeInfoDto;
import cn.hjf.job.model.entity.resume.*;
import cn.hjf.job.model.form.resume.BaseResumeForm;
import cn.hjf.job.model.vo.resume.*;
import cn.hjf.job.resume.mapper.*;
import cn.hjf.job.resume.repository.ProjectDescriptionRepository;
import cn.hjf.job.resume.repository.WorkDescriptionRepository;
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
import java.util.Collections;
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

    @Resource
    private RabbitService rabbitService;

    @Resource
    private ProjectDescriptionRepository projectDescriptionRepository;

    @Resource
    private WorkDescriptionRepository workDescriptionRepository;


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

        if (resumeCount == 0 || baseResumeForm.getResumeInfoForm().getIsDefaultDisplay().equals(1)) {
            rabbitService.sendMessage(MqConst.EXCHANGE_ES, MqConst.ROUTING_ES_RESUME, resumeInfo);
        }

        return resumeInfo.getId();
    }

    @Override
    public List<BaseResumeVo> findBaseResumeList(Long userId) {

        // 查询全部的简历
        LambdaQueryWrapper<ResumeInfo> resumeInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        resumeInfoLambdaQueryWrapper.eq(ResumeInfo::getCandidateId, userId);
        List<ResumeInfo> resumeInfos = resumeInfoMapper.selectList(resumeInfoLambdaQueryWrapper);

        List<Long> resumeIds = new ArrayList<>(resumeInfos.size());

        List<BaseResumeVo> baseResumeVos = resumeInfos.stream().map(resumeInfo -> {
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
                List<BaseProjectExperienceVo> baseProjectExperienceVos = getBaseProjectExperienceVos(projectExperiences, baseResumeVos.get(i).getId());
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

        List<String> projectDescriptions = new ArrayList<>(projectExperiences.size());

        List<ProjectExperienceVo> projectExperienceVos = projectExperiences.stream().map(projectExperience -> {
            ProjectExperienceVo projectExperienceVo = new ProjectExperienceVo();
            BeanUtils.copyProperties(projectExperience, projectExperienceVo);
            projectDescriptions.add(projectExperience.getProjectDescription());
            return projectExperienceVo;
        }).toList();

        List<ProjectDescriptionDoc> descriptionDocs = projectDescriptionRepository.findAllById(projectDescriptions);

        // 将对应的项目描述设置到 ProjectExperience 对象
        projectExperienceVos.forEach(projectExperience -> {
            // 查找对应的项目描述文档
            for (ProjectDescriptionDoc descriptionDoc : descriptionDocs) {
                if (descriptionDoc.getId().equals(projectExperience.getProjectDescription())) {
                    projectExperience.setProjectDescription(descriptionDoc.getDescription()); // 设置项目描述
                    break;
                }
            }
        });

        resumeInfoDto.setProjectExperienceVos(projectExperienceVos);

        // 获取工作经历
        LambdaQueryWrapper<WorkExperience> workExperienceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        workExperienceLambdaQueryWrapper.eq(WorkExperience::getResumeId, resumeId);
        List<WorkExperience> workExperiences = workExperienceMapper.selectList(workExperienceLambdaQueryWrapper);

        List<String> workDescriptions = new ArrayList<>(workExperiences.size());

        List<WorkExperienceVo> workExperienceVos = workExperiences.stream().map(workExperience -> {
            WorkExperienceVo workExperienceVo = new WorkExperienceVo();
            BeanUtils.copyProperties(workExperience, workExperienceVo);
            workDescriptions.add(workExperience.getJobDescription());
            return workExperienceVo;
        }).toList();

        List<WorkDescriptionDoc> workDescriptionDocs = workDescriptionRepository.findAllById(workDescriptions);

        workExperienceVos.forEach(workExperienceVo -> {
            // 查找对应的项目描述文档
            for (WorkDescriptionDoc descriptionDoc : workDescriptionDocs) {
                if (descriptionDoc.getId().equals(workExperienceVo.getJobDescription())) {
                    workExperienceVo.setJobDescription(descriptionDoc.getDescription()); // 设置项目描述
                    break;
                }
            }
        });

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

    @Override
    public Long updateResumeInfo(ResumeVo resumeVo, Long userId) {
        ResumeInfo resumeInfo = getResumeInfo(resumeVo.getId(), userId);
        if (resumeInfo == null) return null;

        LambdaUpdateWrapper<ResumeInfo> resumeInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        resumeInfoLambdaUpdateWrapper.set(ResumeInfo::getResumeName, resumeVo.getResumeName()).set(ResumeInfo::getJobStatus, resumeVo.getJobStatus()).set(ResumeInfo::getPersonalAdvantages, resumeVo.getPersonalAdvantages()).set(ResumeInfo::getProfessionalSkills, resumeVo.getProfessionalSkills()).eq(ResumeInfo::getId, resumeInfo.getId());

        resumeInfoMapper.update(resumeInfoLambdaUpdateWrapper);

        // 如果当前是 ’默认显示‘ 就发送 MQ 消息告知存储到 ES 以供招聘端检索
        // 整体性更新简历到 ES
        rabbitService.sendMessage(MqConst.EXCHANGE_ES, MqConst.ROUTING_ES_RESUME, resumeInfo);
        return resumeVo.getId();
    }

    @Override
    public Long updateEducationBackground(EducationBackgroundVo educationBackgroundVo, Long userId) {
        ResumeInfo resumeInfo = getResumeInfo(educationBackgroundVo.getResumeId(), userId);
        if (resumeInfo == null) return null;

        LambdaUpdateWrapper<EducationBackground> educationBackgroundVoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        educationBackgroundVoLambdaUpdateWrapper.set(EducationBackground::getSchoolName, educationBackgroundVo.getSchoolName()).set(EducationBackground::getMajor, educationBackgroundVo.getMajor()).set(EducationBackground::getEducationLevel, educationBackgroundVo.getEducationLevel()).set(EducationBackground::getIsFullTime, educationBackgroundVo.getIsFullTime()).set(EducationBackground::getStartYear, educationBackgroundVo.getStartYear()).set(EducationBackground::getEndYear, educationBackgroundVo.getEndYear()).eq(EducationBackground::getId, educationBackgroundVo.getId());

        educationBackgroundMapper.update(educationBackgroundVoLambdaUpdateWrapper);

        rabbitService.sendMessage(MqConst.EXCHANGE_ES, MqConst.ROUTING_ES_RESUME, resumeInfo);

        return educationBackgroundVo.getId();
    }

    @Override
    public Long updateJobExpectation(JobExpectationVo jobExpectationVo, Long userId) {
        ResumeInfo resumeInfo = getResumeInfo(jobExpectationVo.getResumeId(), userId);
        if (resumeInfo == null) return null;

        LambdaUpdateWrapper<JobExpectation> jobExpectationLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        jobExpectationLambdaUpdateWrapper.set(JobExpectation::getJobType, jobExpectationVo.getJobType()).set(JobExpectation::getSalaryMax, jobExpectationVo.getSalaryMax()).set(JobExpectation::getSalaryMin, jobExpectationVo.getSalaryMin()).set(JobExpectation::getIsNegotiable, jobExpectationVo.getIsNegotiable()).eq(JobExpectation::getId, jobExpectationVo.getId());

        jobExpectationMapper.update(jobExpectationLambdaUpdateWrapper);

        rabbitService.sendMessage(MqConst.EXCHANGE_ES, MqConst.ROUTING_ES_RESUME, resumeInfo);

        return jobExpectationVo.getId();
    }

    @Override
    public Long addProjectExperience(ProjectExperienceVo projectExperienceVo, Long userId) {
        ResumeInfo resumeInfo = getResumeInfo(projectExperienceVo.getResumeId(), userId);
        if (resumeInfo == null) return null;

        // 将项目描述保存到 mongoDB
        ProjectDescriptionDoc projectDescriptionDoc = new ProjectDescriptionDoc();
        projectDescriptionDoc.setDescription(projectExperienceVo.getProjectDescription());
        ProjectDescriptionDoc saveProjectDescriptionDoc = projectDescriptionRepository.save(projectDescriptionDoc);

        ProjectExperience projectExperience = new ProjectExperience();
        BeanUtils.copyProperties(projectExperienceVo, projectExperience);
        projectExperience.setId(null);
        projectExperience.setProjectDescription(saveProjectDescriptionDoc.getId());

        int insert = projectExperienceMapper.insert(projectExperience);

        return insert == 1 ? projectExperience.getId() : null;
    }

    @Override
    public Boolean deleteProjectExperience(Long resumeId, @NotNull Long projectId, Long userId) {
        ResumeInfo resumeInfo = getResumeInfo(resumeId, userId);
        if (resumeInfo == null) return null;
        LambdaQueryWrapper<ProjectExperience> projectExperienceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectExperienceLambdaQueryWrapper.eq(ProjectExperience::getId, projectId)
                .eq(ProjectExperience::getResumeId, resumeId);
        int i = projectExperienceMapper.delete(projectExperienceLambdaQueryWrapper);
        return i == 1;
    }

    @Override
    public Long addWorkExperience(WorkExperienceVo workExperienceVo, Long userId) {
        ResumeInfo resumeInfo = getResumeInfo(workExperienceVo.getResumeId(), userId);
        if (resumeInfo == null) return null;

        WorkDescriptionDoc workDescriptionDoc = new WorkDescriptionDoc();
        workDescriptionDoc.setDescription(workExperienceVo.getJobDescription());
        WorkDescriptionDoc saveWorkDescriptionDoc = workDescriptionRepository.save(workDescriptionDoc);

        WorkExperience workExperience = new WorkExperience();
        BeanUtils.copyProperties(workExperienceVo, workExperience);
        workExperience.setId(null);
        workExperience.setJobDescription(saveWorkDescriptionDoc.getId());

        int insert = workExperienceMapper.insert(workExperience);

        return insert == 1 ? workExperience.getId() : null;
    }

    @Override
    public Boolean deleteWorkExperience(Long resumeId, @NotNull Long workId, Long userId) {
        ResumeInfo resumeInfo = getResumeInfo(resumeId, userId);
        if (resumeInfo == null) return null;
        LambdaQueryWrapper<WorkExperience> workExperienceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        workExperienceLambdaQueryWrapper.eq(WorkExperience::getId, workId)
                .eq(WorkExperience::getResumeId, resumeId);

        int delete = workExperienceMapper.delete(workExperienceLambdaQueryWrapper);
        return delete == 1;
    }

    @Override
    public Long addHonorAward(HonorAwardVo honorAwardVo, Long userId) {
        ResumeInfo resumeInfo = getResumeInfo(honorAwardVo.getResumeId(), userId);
        if (resumeInfo == null) return null;

        HonorAward honorAward = new HonorAward();
        BeanUtils.copyProperties(honorAwardVo, honorAward);
        honorAward.setId(null);
        int insert = honorAwardMapper.insert(honorAward);

        return insert == 1 ? honorAward.getId() : null;
    }

    @Override
    public Boolean deleteHonorAward(Long resumeId, @NotNull Long honorId, Long userId) {
        ResumeInfo resumeInfo = getResumeInfo(resumeId, userId);
        if (resumeInfo == null) return null;

        LambdaQueryWrapper<HonorAward> honorAwardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        honorAwardLambdaQueryWrapper.eq(HonorAward::getId, honorId)
                .eq(HonorAward::getResumeId, resumeId);

        int delete = honorAwardMapper.delete(honorAwardLambdaQueryWrapper);

        return delete == 1;
    }

    @Override
    public Long addCertification(CertificationVo certificationVo, Long userId) {
        ResumeInfo resumeInfo = getResumeInfo(certificationVo.getResumeId(), userId);
        if (resumeInfo == null) return null;

        Certification certification = new Certification();
        BeanUtils.copyProperties(certificationVo, certification);
        certification.setId(null);

        int insert = certificationMapper.insert(certification);

        return certification.getId();
    }

    @Override
    public Boolean deleteCertification(Long resumeId, Long certificationId, Long userId) {
        ResumeInfo resumeInfo = getResumeInfo(resumeId, userId);
        if (resumeInfo == null) return null;
        LambdaQueryWrapper<Certification> certificationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        certificationLambdaQueryWrapper.eq(Certification::getId, certificationId)
                .eq(Certification::getResumeId, resumeId);

        int delete = certificationMapper.delete(certificationLambdaQueryWrapper);

        return delete == 1;
    }

    private ResumeInfo getResumeInfo(@NotNull Long resumeId, @NotNull Long userId) {
        LambdaQueryWrapper<ResumeInfo> resumeInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        resumeInfoLambdaQueryWrapper.eq(ResumeInfo::getId, resumeId).eq(ResumeInfo::getCandidateId, userId);
        return resumeInfoMapper.selectOne(resumeInfoLambdaQueryWrapper);
    }

    @NotNull
    private static List<BaseProjectExperienceVo> getBaseProjectExperienceVos(List<ProjectExperience> projectExperiences, Long resumeId) {
        List<BaseProjectExperienceVo> baseProjectExperienceVos = new ArrayList<>();
        for (ProjectExperience projectExperience : projectExperiences) {
            if (Objects.equals(projectExperience.getResumeId(), resumeId)) {
                BaseProjectExperienceVo baseProjectExperienceVo = new BaseProjectExperienceVo(projectExperience.getId(), projectExperience.getProjectName(), projectExperience.getRole());
                baseProjectExperienceVos.add(baseProjectExperienceVo);
            }
        }
        return baseProjectExperienceVos;
    }
}
















