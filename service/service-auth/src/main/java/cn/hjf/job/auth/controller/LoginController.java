package cn.hjf.job.auth.controller;

import cn.hjf.job.model.form.auth.EmailCodeLoginForm;
import cn.hjf.job.model.form.auth.PhoneCodeLoginForm;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录接口
 *
 * @author hjf
 * @version 1.0
 * @description
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    /**
     * 招聘端邮件验证码登录
     *
     * @param emailCodeLoginForm 邮件验证码登录表单
     * @return String
     */
    @PostMapping("/recruiter/email/code")
    public String recruiterEmailCode(@RequestBody EmailCodeLoginForm emailCodeLoginForm) {
        return "success";
    }


    /**
     * 应聘端邮件验证码登录
     *
     * @param emailCodeLoginForm 邮件验证码登录表单
     * @return String
     */
    @PostMapping("/candidate/email/code")
    public String candidateEmailCode(@RequestBody EmailCodeLoginForm emailCodeLoginForm) {
        return "success";
    }

    /**
     * 应聘端手机号验证码登录
     *
     * @param phoneCodeLoginForm 手机号验证码登录表单
     * @return String
     */
    @PostMapping("/candidate/phone/code")
    public String candidatePhoneCode(@RequestBody PhoneCodeLoginForm phoneCodeLoginForm) {
        return "success";
    }


    /**
     * 招聘端手机号验证码登录
     *
     * @param phoneCodeLoginForm 手机号验证码登录表单
     * @return String
     */
    @PostMapping("/recruiter/phone/code")
    public String recruiterPhoneCode(@RequestBody PhoneCodeLoginForm phoneCodeLoginForm) {
        return "success";
    }
}
