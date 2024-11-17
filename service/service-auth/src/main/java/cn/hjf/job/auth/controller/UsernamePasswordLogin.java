package cn.hjf.job.auth.controller;

import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.form.auth.LoginInfoForm;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户名密码登录服务
 *
 * @author hjf
 * @version 1.0
 * @description 用户名（手机号/邮箱）与密码登录控制器
 */

@RestController
@RequestMapping("/auth")
public class UsernamePasswordLogin {

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 登录
     *
     * @param loginInfoForm 登录信息
     * @return JwtToken
     */
    @PostMapping("/login")
    public Object login(@RequestBody @Validated LoginInfoForm loginInfoForm) {
        return null;
    }

    /**
     * 退出登录
     *
     * @return 退出信息
     */
    @PostMapping("/out")
    @PreAuthorize("hasAnyRole('ROLE_CANDIDATE','ROLE_RECRUITER')")
    public Result<String> outLogin() {
        // 获取用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.parseLong(authentication.getName());
        Boolean b = redisTemplate.delete(RedisConstant.USER_TOKEN + id);
        return Boolean.TRUE.equals(b) ? Result.ok("退出成功") : Result.fail("退出失败");
    }
}
