package cn.hjf.job.resume.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.dto.resume.ResumeInfoDto;
import cn.hjf.job.model.form.resume.BaseResumeForm;
import cn.hjf.job.model.request.resume.ResumeSearchPageParam;
import cn.hjf.job.model.vo.base.PageEsVo;
import cn.hjf.job.model.vo.resume.*;
import cn.hjf.job.resume.service.ResumeInfoService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
    public Result<Long> createBaseResume(@RequestBody BaseResumeForm baseResumeForm, Principal principal) {
        try {
            Long resumeId = resumeInfoService.createBaseResume(baseResumeForm, Long.parseLong(principal.getName()));
            return resumeId != null ? Result.ok(resumeId) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }


    /**
     * 查询用户的全部职位
     *
     * @param principal 用户信息
     * @return Result<List < BaseResumeVo>>
     */
    @GetMapping("/base/list")
    public Result<List<BaseResumeVo>> findBaseResumeList(Principal principal) {
        try {
            long startTime = System.currentTimeMillis();  // 开始计时
            List<BaseResumeVo> baseResumeVos = resumeInfoService.findBaseResumeList(Long.parseLong(principal.getName()));
            long endTime = System.currentTimeMillis();  // 结束计时
            System.out.println("Execution Time: " + (endTime - startTime) + " milliseconds");
            return baseResumeVos != null ? Result.ok(baseResumeVos) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 获取简历信息
     *
     * @param resumeId  简历 id
     * @param principal 用户信息
     * @return Result<ResumeInfoDto>
     */
    @GetMapping("/info/{resumeId}")
    public Result<ResumeInfoDto> getResumeById(@PathVariable(name = "resumeId") Long resumeId, Principal principal) {
        try {
            long startTime = System.currentTimeMillis();
            ResumeInfoDto resumeInfoDto = resumeInfoService.getResumeInfoById(resumeId, Long.parseLong(principal.getName()));
            long endTime = System.currentTimeMillis();
            System.out.println("getResumeInfoById 耗时:" + (endTime - startTime));
            return resumeInfoDto != null ? Result.ok(resumeInfoDto) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 更新 resume
     *
     * @param resumeVo  简历基本信息
     * @param principal 用户信息
     * @return 简历 id
     */
    @PutMapping("/resumeInfo")
    public Result<Long> updateResumeInfo(@RequestBody ResumeVo resumeVo, Principal principal) {
        try {
            Long id = resumeInfoService.updateResumeInfo(resumeVo, Long.parseLong(principal.getName()));
            return id != null ? Result.ok(id) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 更新 教育背景
     *
     * @param educationBackgroundVo 教育背景
     * @param principal             用户信息
     * @return 主键 id
     */
    @PutMapping("/educationBackground")
    public Result<Long> updateEducationBackground(@RequestBody EducationBackgroundVo educationBackgroundVo, Principal principal) {
        try {
            Long id = resumeInfoService.updateEducationBackground(educationBackgroundVo, Long.parseLong(principal.getName()));
            return id != null ? Result.ok(id) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 更新工作期望
     *
     * @param jobExpectationVo 工作期望
     * @param principal        用户信息
     * @return 主键 id
     */
    @PutMapping("/jobExpectationVo")
    public Result<Long> updateJobExpectation(@RequestBody JobExpectationVo jobExpectationVo, Principal principal) {
        try {
            Long id = resumeInfoService.updateJobExpectation(jobExpectationVo, Long.parseLong(principal.getName()));
            return id != null ? Result.ok(id) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 添加 项目经验
     *
     * @param projectExperienceVo 项目经验
     * @param principal           用户信息
     * @return 主键 id
     */
    @PostMapping("/projectExperience")
    public Result<Long> addProjectExperience(@RequestBody ProjectExperienceVo projectExperienceVo, Principal principal) {
        try {
            Long id = resumeInfoService.addProjectExperience(projectExperienceVo, Long.parseLong(principal.getName()));
            return id != null ? Result.ok(id) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }


    /**
     * 删除 项目经验
     *
     * @param resumeId  简历 id
     * @param projectId 项目 id
     * @param principal 用户信息
     * @return 是否成功
     */
    @DeleteMapping("/projectExperience")
    public Result<String> deleteProjectExperience(@RequestParam Long resumeId, @RequestParam Long projectId, Principal principal) {
        try {
            Boolean isSuccess = resumeInfoService.deleteProjectExperience(resumeId, projectId, Long.parseLong(principal.getName()));
            return isSuccess ? Result.ok("删除成功") : Result.fail("删除失败");
        } catch (Exception e) {
            return Result.fail("删除失败");
        }
    }


    /**
     * 添加工作经验
     *
     * @param workExperienceVo 工作经验
     * @param principal        用户信息
     * @return 主键
     */
    @PostMapping("/workExperience")
    public Result<Long> addWorkExperience(@RequestBody WorkExperienceVo workExperienceVo, Principal principal) {
        try {
            Long id = resumeInfoService.addWorkExperience(workExperienceVo, Long.parseLong(principal.getName()));
            return id != null ? Result.ok(id) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 删除工作经历
     *
     * @param resumeId  简历 id
     * @param workId    工作经历 id
     * @param principal 用户信息
     * @return 是否成功
     */
    @DeleteMapping("/workExperience")
    public Result<String> deleteWorkExperience(@RequestParam Long resumeId, @RequestParam Long workId, Principal principal) {
        try {
            Boolean isSuccess = resumeInfoService.deleteWorkExperience(resumeId, workId, Long.parseLong(principal.getName()));
            return isSuccess ? Result.ok("删除成功") : Result.fail("删除失败");
        } catch (Exception e) {
            return Result.fail("删除失败");
        }
    }

    /**
     * 添加荣誉奖励
     *
     * @param honorAwardVo 荣誉奖励
     * @param principal    用户信息
     * @return 主键 id
     */
    @PostMapping("/honorAward")
    public Result<Long> addHonorAward(@RequestBody HonorAwardVo honorAwardVo, Principal principal) {
        try {
            Long id = resumeInfoService.addHonorAward(honorAwardVo, Long.parseLong(principal.getName()));
            return id != null ? Result.ok(id) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 删除荣誉奖励
     *
     * @param resumeId  奖励 id
     * @param honorId   荣誉奖励 id
     * @param principal 用户信息
     * @return 是否成功
     */
    @DeleteMapping("/honorAward")
    public Result<String> deleteHonorAward(@RequestParam Long resumeId, @RequestParam Long honorId, Principal principal) {
        try {
            Boolean isSuccess = resumeInfoService.deleteHonorAward(resumeId, honorId, Long.parseLong(principal.getName()));
            return isSuccess ? Result.ok("删除成功") : Result.fail("删除失败");
        } catch (Exception e) {
            return Result.fail("删除失败");
        }
    }

    /**
     * 添加证书
     *
     * @param certificationVo 证书
     * @param principal       用户信息
     * @return 主键 id
     */
    @PostMapping("/certification")
    public Result<Long> addCertification(@RequestBody CertificationVo certificationVo, Principal principal) {
        try {
            Long id = resumeInfoService.addCertification(certificationVo, Long.parseLong(principal.getName()));
            return id != null ? Result.ok(id) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 删除证书
     *
     * @param resumeId        简历 id
     * @param certificationId 证书 id
     * @param principal       用户信息
     * @return 是否成功
     */
    @DeleteMapping("/certification")
    public Result<String> deleteCertification(@RequestParam Long resumeId, @RequestParam Long certificationId, Principal principal) {
        try {
            Boolean isSuccess = resumeInfoService.deleteCertification(resumeId, certificationId, Long.parseLong(principal.getName()));
            return isSuccess ? Result.ok("删除成功") : Result.fail("删除失败");
        } catch (Exception e) {
            return Result.fail("删除失败");
        }
    }


    /**
     * 删除简历
     *
     * @param resumeId  简历 id
     * @param principal 用户信息
     * @return 是否成功
     */
    @DeleteMapping("/info")
    public Result<String> deleteResumeInfo(@RequestParam Long resumeId, Principal principal) {
        try {
            Boolean isSuccess = resumeInfoService.deleteResumeInfo(resumeId, Long.parseLong(principal.getName()));
            return isSuccess ? Result.ok("删除成功") : Result.fail("删除失败");
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 设置简历成为默认显示
     *
     * @param resumeId  简历 id
     * @param principal 用户信息
     * @return 是否成功
     */
    @PutMapping("/defaultDisplay")
    public Result<String> setResumeDefaultDisplay(@RequestParam Long resumeId, Principal principal) {
        try {
            Boolean isSuccess
                    = resumeInfoService.setResumeDefaultDisplay(resumeId, Long.parseLong(principal.getName()));

            return isSuccess ? Result.ok("更改成功") : Result.fail("更改失败");
        } catch (Exception e) {
            return Result.fail("更改失败");
        }
    }

    /**
     * 招聘端搜索人才
     *
     * @param limit                 每页记录数量
     * @param resumeSearchPageParam 参数
     * @return Result<PageEsVo < ResumeVoEs>>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_RECRUITER')")
    @GetMapping("/recruiter/base/{limit}")
    public Result<PageEsVo<ResumeVoEs>> searchBaseResumeInfoPage(
            @PathVariable(name = "limit") Integer limit,
            ResumeSearchPageParam resumeSearchPageParam
    ) {
        try {
            PageEsVo<ResumeVoEs> resumeVoEsPageEsVo = resumeInfoService.searchBaseResumeInfoPage(limit, resumeSearchPageParam);
            return Result.ok(resumeVoEsPageEsVo);
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 招聘端获取用户的默认简历（基本信息）
     *
     * @param candidateId 用户 id
     * @return Result<ResumeVoEs>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_RECRUITER')")
    @GetMapping("/candidate/base/resume/{candidateId}")
    public Result<ResumeVoEs> getResumeVoEsByUserId(@PathVariable Long candidateId) {
        ResumeVoEs resumeVoEs = resumeInfoService.getResumeVoEsByUserId(candidateId);
        if (resumeVoEs != null) {
            return Result.ok(resumeVoEs);
        }
        return Result.fail();
    }
}












