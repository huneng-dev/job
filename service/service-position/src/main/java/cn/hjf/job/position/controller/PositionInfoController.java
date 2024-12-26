package cn.hjf.job.position.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.entity.position.PositionInfo;
import cn.hjf.job.model.form.position.PositionInfoForm;
import cn.hjf.job.model.request.position.CandidatePositionPageParam;
import cn.hjf.job.model.vo.base.PagePositionEsVo;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.position.CandidateBasePositionInfoVo;
import cn.hjf.job.model.vo.position.RecruiterBasePositionInfoVo;
import cn.hjf.job.model.vo.position.RecruiterPositionInfoVo;
import cn.hjf.job.position.service.PositionInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * 职位管理
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController
@RequestMapping("/positionInfo")
public class PositionInfoController {


    @Resource(name = "positionInfoServiceImpl")
    private PositionInfoService positionInfoService;

    /**
     * 创建职位
     *
     * @param positionInfoForm 职位表单
     * @param principal        用户
     * @return 信息
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN_RECRUITER')")
    public Result<String> create(@RequestBody PositionInfoForm positionInfoForm, Principal principal) {
        try {
            boolean b = positionInfoService.create(positionInfoForm, Long.parseLong(principal.getName()));
            if (b) {
                return Result.ok("创建职位成功");
            }
            return Result.fail("创建职位失败");
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }


    /**
     * 招聘端基本职位信息分页
     *
     * @param positionName 职位名 (可以模糊查询)
     * @param status       状态 , 0: 无状态
     * @param page         页
     * @param limit        没页多少
     * @param principal    用户信息
     * @return Result<PageVo < RecruiterBasePositionInfoVo>>
     */
    @GetMapping("/recruiter/base/{page}/{limit}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_RECRUITER','ROLE_EMPLOYEE_RECRUITER')")
    public Result<PageVo<RecruiterBasePositionInfoVo>> findRecruiterBasePositionInfoByUserId(@RequestParam String positionName, @RequestParam Integer status, @PathVariable(name = "page") Integer page, @PathVariable(name = "limit") Integer limit, Principal principal) {
        Page<PositionInfo> positionInfoPage = new Page<>(page, limit);

        PageVo<RecruiterBasePositionInfoVo> recruiterBasePositionInfo = positionInfoService.findRecruiterBasePositionInfoByUserId(positionInfoPage, positionName, status, Long.parseLong(principal.getName()));

        return Result.ok(recruiterBasePositionInfo);
    }

    /**
     * 获取招聘端职位详情
     *
     * @param positionId 职位id
     * @param principal  用户信息
     * @return Result<RecruiterPositionInfoVo>
     */
    @GetMapping("/{positionId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_RECRUITER','ROLE_EMPLOYEE_RECRUITER')")
    public Result<RecruiterPositionInfoVo> getRecruiterPositionInfoVoById(@PathVariable(name = "positionId") Long positionId, Principal principal) {
        try {
            RecruiterPositionInfoVo recruiterPositionInfoVo = positionInfoService.getRecruiterPositionInfoVoById(positionId, Long.parseLong(principal.getName()));
            return Result.ok(recruiterPositionInfoVo);
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 设置职位为开放状态
     *
     * @param positionId 职位 id
     * @param principal  用户信息
     * @return Result<String>
     */
    @PatchMapping("/status/open/{positionId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_RECRUITER','ROLE_EMPLOYEE_RECRUITER')")
    public Result<String> setPositionStatusToOpen(@PathVariable(name = "positionId") Long positionId, Principal principal) {
        try {
            boolean isSuccess = positionInfoService.setPositionStatusToOpen(positionId, Long.parseLong(principal.getName()));
            return isSuccess ? Result.ok("成功") : Result.fail("失败");
        } catch (Exception e) {
            return Result.fail("失败:" + e.getMessage());
        }
    }

    /**
     * 设置职位状态为待开放状态
     *
     * @param positionId 职位 id
     * @param principal  用户信息
     * @return Result<String>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_RECRUITER','ROLE_EMPLOYEE_RECRUITER')")
    @PatchMapping("/status/noOpen/{positionId}")
    public Result<String> setPositionStatusToNoOpen(@PathVariable(name = "positionId") Long positionId, Principal principal) {
        try {
            boolean isSuccess = positionInfoService.setPositionStatusToNoOpen(positionId, Long.parseLong(principal.getName()));
            return isSuccess ? Result.ok("成功") : Result.fail("失败");
        } catch (Exception e) {
            return Result.fail("失败:" + e.getMessage());
        }
    }

    /**
     * 设置职位状态为关闭状态状态
     *
     * @param positionId 职位 id
     * @param principal  用户信息
     * @return Result<String>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_RECRUITER','ROLE_EMPLOYEE_RECRUITER')")
    @PatchMapping("/status/close/{positionId}")
    public Result<String> setPositionStatusToClose(@PathVariable(name = "positionId") Long positionId, Principal principal) {
        try {
            boolean isSuccess = positionInfoService.setPositionStatusToClose(positionId, Long.parseLong(principal.getName()));
            return isSuccess ? Result.ok("成功") : Result.fail("失败");
        } catch (Exception e) {
            return Result.fail("失败:" + e.getMessage());
        }
    }

    /**
     * 删除职位
     *
     * @param positionId 职位 id
     * @param principal  用户信息
     * @return Result<String>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_RECRUITER','ROLE_EMPLOYEE_RECRUITER')")
    @DeleteMapping("/{positionId}")
    public Result<String> deletePositionById(@PathVariable(name = "positionId") Long positionId, Principal principal) {
        try {
            boolean isSuccess = positionInfoService.deletePositionById(positionId, Long.parseLong(principal.getName()));
            return isSuccess ? Result.ok("成功") : Result.fail("失败");
        } catch (Exception e) {
            return Result.fail("失败:" + e.getMessage());
        }
    }


    /**
     * 应聘端基本职位信息分页
     *
     * @param limit                      每页多少
     * @param candidatePositionPageParam 应聘端职位分页参数
     * @return Result<PagePositionEsVo < CandidateBasePositionInfoVo>>
     */
    @GetMapping("/candidate/base/{limit}")
    public Result<PagePositionEsVo<CandidateBasePositionInfoVo>> searchCandidateBasePositionInfo(
            @PathVariable Integer limit,
            CandidatePositionPageParam candidatePositionPageParam
    ) {
        PagePositionEsVo<CandidateBasePositionInfoVo> candidateBasePositionInfoVoPagePositionEsVo = positionInfoService.searchCandidateBasePositionInfo(limit, candidatePositionPageParam);
        return Result.ok(candidateBasePositionInfoVoPagePositionEsVo);
    }
}
