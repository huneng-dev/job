package cn.hjf.job.resume.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.form.resume.BaseResumeForm;
import cn.hjf.job.model.form.resume.EducationBackgroundForm;
import cn.hjf.job.model.form.resume.JobExpectationForm;
import cn.hjf.job.model.form.resume.ResumeInfoForm;
import cn.hjf.job.resume.service.ResumeInfoService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 简历控制器
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController
@RequestMapping("/resumeInfo")
public class ResumeInfoController {

    @Resource(name = "resumeInfoServiceImpl")
    private ResumeInfoService resumeInfoService;


    /**
     * 创建基础简历
     *
     * @param baseResumeForm 基础简历
     * @param principal      用户信息
     * @return 简历 id
     */
    @PostMapping("/create/base")
    @PreAuthorize("hasRole('ROLE_USER_CANDIDATE')")
    public Result<Long> createBaseResume(
            @RequestBody BaseResumeForm baseResumeForm,
            Principal principal
    ) {
        try {
            Long resumeId = resumeInfoService.createBaseResume(baseResumeForm, Long.parseLong(principal.getName()));
            return resumeId != null ? Result.ok(resumeId) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }
}
