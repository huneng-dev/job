package cn.hjf.job.resume.service.impl;

import cn.hjf.job.model.entity.resume.EducationBackground;
import cn.hjf.job.model.entity.resume.JobExpectation;
import cn.hjf.job.model.entity.resume.ResumeInfo;
import cn.hjf.job.model.form.resume.BaseResumeForm;
import cn.hjf.job.resume.mapper.EducationBackgroundMapper;
import cn.hjf.job.resume.mapper.JobExpectationMapper;
import cn.hjf.job.resume.mapper.ResumeInfoMapper;
import cn.hjf.job.resume.service.ResumeInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
            resumeInfoLambdaUpdateWrapper.set(ResumeInfo::getIsDefaultDisplay, 0)
                    .eq(ResumeInfo::getCandidateId, userId).ne(ResumeInfo::getId, resumeInfo.getId());

            resumeInfoMapper.update(resumeInfoLambdaUpdateWrapper); // 如果出异常也会正常回滚
        }
        return resumeInfo.getId();
    }
}
















