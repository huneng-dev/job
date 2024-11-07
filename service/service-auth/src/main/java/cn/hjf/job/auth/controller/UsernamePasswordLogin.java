package cn.hjf.job.auth.controller;

import cn.hjf.job.model.form.auth.LoginInfoForm;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author hjf
 * @version 1.0
 * @description 用户名（手机号/邮箱）与密码登录控制器
 */

@RestController
@RequestMapping("/auth")
public class UsernamePasswordLogin {

    @PostMapping("/login")
    public Object login(@RequestBody @Validated LoginInfoForm loginInfoForm) {
        return null;
    }
}
