package cn.hjf.job.user.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.request.EmailRequest;
import cn.hjf.job.model.request.PhoneRequest;
import cn.hjf.job.user.service.ValidateCodeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码控制器
 *
 * @author hjf
 * @version 1.0
 * @description 发送邮件验证码，手机号验证码
 */

@RequestMapping("/validate-code")
@RestController
public class ValidateCodeController {

    @Resource
    private ValidateCodeService validateCodeService;

    /**
     * 发送邮箱注册验证码
     *
     * @return 验证码发送状态
     */
    @PostMapping("/email-register")
    public Result<String> sendRegisterEmailCode(@RequestBody EmailRequest emailRequest) {
        return validateCodeService.sendEmailRegisterValidateCode(emailRequest.getEmail());
    }

    /**
     * 发送邮箱找回密码验证码
     *
     * @param emailRequest 邮箱请求对象，包含邮箱地址
     * @return 验证码发送状态
     */
    @PostMapping("/email-recovery")
    public Result<String> sendRecoveryEmailCode(@RequestBody EmailRequest emailRequest) {
        return validateCodeService.sendEmailRecoveryValidateCode(emailRequest.getEmail());
    }

    /**
     * 发送邮箱登录验证码
     *
     * @param emailRequest 邮箱请求对象，包含邮箱地址
     * @return 验证码发送状态
     */
    @PostMapping("/email-login")
    public Result<String> sendLoginEmailCode(@RequestBody EmailRequest emailRequest) {
        return validateCodeService.sendEmailLoginValidateCode(emailRequest.getEmail());
    }

    /**
     * 发送手机号注册验证码
     *
     * @param phoneRequest 手机号
     * @return 验证码发送状态
     */
    @PostMapping("/phone-register")
    public Result<String> sendRegisterPhoneCode(@RequestBody PhoneRequest phoneRequest) {
        return validateCodeService.sendPhoneRegisterValidateCode(phoneRequest.getPhone());
    }

    /**
     * 发送手机号找回密码验证码
     *
     * @param phoneRequest 手机号
     * @return 验证码发送状态
     */
    @PostMapping("/phone-recovery")
    public Result<String> sendRecoveryPhoneCode(@RequestBody PhoneRequest phoneRequest) {
        return validateCodeService.sendPhoneRecoveryValidateCode(phoneRequest.getPhone());
    }

    /**
     * 发送手机号登录验证码
     *
     * @param phoneRequest 手机号
     * @return 验证码发送状态
     */
    @PostMapping("/phone-login")
    public Result<String> sendLoginPhoneCode(@RequestBody PhoneRequest phoneRequest) {
        return validateCodeService.sendPhoneLoginValidateCode(phoneRequest.getPhone());
    }
}
