package cn.hjf.job.position.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.form.position.PositionInfoForm;
import cn.hjf.job.position.service.PositionInfoService;
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
}
