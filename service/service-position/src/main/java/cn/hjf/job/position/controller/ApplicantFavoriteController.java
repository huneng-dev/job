package cn.hjf.job.position.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.position.service.ApplicantFavoriteService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * 职位喜欢控制器
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController
@RequestMapping("/favorite")
public class ApplicantFavoriteController {

    @Resource(name = "applicantFavoriteServiceImpl")
    private ApplicantFavoriteService applicantFavoriteService;

    /**
     * 关注职位 / 喜欢职位 / 收藏职位
     *
     * @param positionId 职位 id
     * @param principal  用户 信息
     * @return 是否成功
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER_CANDIDATE')")
    public Result<String> addPositionToFavorite(@RequestParam Long positionId, Principal principal) {
        try {
            boolean isSuccess = applicantFavoriteService.addPositionToFavorite(positionId, Long.parseLong(principal.getName()));
            if (isSuccess) {
                return Result.ok("成功");
            } else {
                return Result.fail("失败");
            }
        } catch (Exception e) {
            return Result.fail("收藏异常");
        }
    }


    /**
     * 删除喜欢的职位
     *
     * @param positionId 职位 id
     * @param principal  用户信息
     * @return 是否成功
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_USER_CANDIDATE')")
    public Result<String> deletePositionFavorite(@RequestParam Long positionId, Principal principal) {
        try {
            boolean isSuccess = applicantFavoriteService.deletePositionFavorite(positionId, Long.parseLong(principal.getName()));
            if (isSuccess) {
                return Result.ok("成功");
            } else {
                return Result.fail("失败");
            }
        } catch (Exception e) {
            return Result.fail("范围异常");
        }
    }


    /**
     * 是否关注
     *
     * @param positionId 职位 id
     * @param principal  用户信息
     * @return 是否关注
     */
    @GetMapping("/isFavorite")
    @PreAuthorize("hasRole('ROLE_USER_CANDIDATE')")
    public Result<Boolean> isPositionFavorite(@RequestParam Long positionId, Principal principal) {
        try {
            boolean positionFavorite = applicantFavoriteService.isPositionFavorite(positionId, Long.parseLong(principal.getName()));
            return Result.ok(positionFavorite);
        } catch (Exception e) {
            return Result.fail();
        }
    }
}
