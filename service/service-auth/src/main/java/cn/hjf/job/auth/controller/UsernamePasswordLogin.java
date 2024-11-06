package cn.hjf.job.auth.controller;

import cn.hjf.job.common.constant.LoginMethodConstant;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.form.auth.LoginInfoForm;
import cn.hjf.job.model.query.auth.LoginInfoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

/**
 * @author hjf
 * @version 1.0
 * @description 用户名（手机号/邮箱）与密码登录控制器
 */

@RestController
@RequestMapping("/auth")
public class UsernamePasswordLogin {

//    private final AuthenticationManager authenticationManager;
//
//    public UsernamePasswordLogin(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }

    @PostMapping("/login")
    public Object login(@RequestBody LoginInfoForm loginInfoForm) {
//        Authentication authenticationRequest = null;
//
//        if (loginInfoForm.getLoginMethod().equals(LoginMethodConstant.PHONE)) {
//            authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginInfoForm.getPhone(), loginInfoForm.getPassword());
//        }
//
//        try {
//            Authentication authenticationResponse =
//                    this.authenticationManager.authenticate(authenticationRequest);
//
//            return Result.ok(new LoginInfoQuery("登录成功", "头像", authenticationResponse.toString()));
//        } catch (AuthenticationException e) {
//            return Result.build(new LoginInfoQuery(),401,"登录失败");
//        }
        return null;
    }
}
