package cn.hjf.job.position.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.entity.position.PositionInfo;
import cn.hjf.job.model.form.position.PositionInfoForm;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.position.RecruiterBasePositionInfoVo;
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
    public Result<PageVo<RecruiterBasePositionInfoVo>> findRecruiterBasePositionInfoByUserId(
            @RequestParam String positionName,
            @RequestParam Integer status,
            @PathVariable(name = "page") Integer page,
            @PathVariable(name = "limit") Integer limit,
            Principal principal
    ) {
        Page<PositionInfo> positionInfoPage = new Page<>(page, limit);

        PageVo<RecruiterBasePositionInfoVo> recruiterBasePositionInfo = positionInfoService.findRecruiterBasePositionInfoByUserId(positionInfoPage, positionName, status, Long.parseLong(principal.getName()));

        return Result.ok(recruiterBasePositionInfo);
    }
}
