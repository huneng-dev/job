package cn.hjf.job.resume.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.resume.service.ResumeFavoriteService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * 简历收藏管理
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController
@RequestMapping("/resumeFavorite")
public class ResumeFavoriteController {

    @Resource
    private ResumeFavoriteService resumeFavoriteService;

    /**
     * 简历是否收藏
     *
     * @param resumeId  简历 id
     * @param principal 用户信息
     * @return Result<Boolean>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_RECRUITER')")
    @GetMapping("/status/{resumeId}")
    public Result<Boolean> getResumeFavoriteStatus(@PathVariable(name = "resumeId") Long resumeId, Principal principal) {
        try {
            Boolean isFavorites = resumeFavoriteService.getResumeFavoriteStatus(resumeId, Long.parseLong(principal.getName()));
            return isFavorites != null ? Result.ok(isFavorites) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 收藏 简历
     *
     * @param resumeId  简历 id
     * @param principal 用户信息
     * @return Result<String>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_RECRUITER')")
    @PostMapping("/add")
    public Result<Boolean> favoriteResume(@RequestParam Long resumeId, Principal principal) {
        try {
            Boolean isSuccess = resumeFavoriteService.favoriteResume(resumeId, Long.parseLong(principal.getName()));
            return isSuccess != null ? Result.ok(isSuccess) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 取消收藏
     *
     * @param resumeId  简历 id
     * @param principal 用户信息
     * @return Result<Boolean>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_RECRUITER')")
    @DeleteMapping("/cancel/{resumeId}")
    public Result<Boolean> cancelResumeFavorite(@PathVariable Long resumeId, Principal principal) {
        try {
            Boolean isSuccess = resumeFavoriteService.cancelResumeFavorite(resumeId, Long.parseLong(principal.getName()));
            return isSuccess != null ? Result.ok(isSuccess) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }



}
