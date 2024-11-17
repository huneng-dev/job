package cn.hjf.job.user.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.enums.auth.VerifyInfoType;
import cn.hjf.job.model.form.user.EmailPasswordVerifyForm;
import cn.hjf.job.model.form.user.PhonePasswordVerifyForm;
import cn.hjf.job.model.query.user.UserVerifyQuery;
import cn.hjf.job.user.service.UserInfoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 密码校验
 * @author hjf
 * @version 1.0
 * @description 密码校验控制器只为内部使用
 */

@RestController
@RequestMapping("/verify")
public class PasswordVerifyController {

    @Resource
    private UserInfoService userInfoService;


    /**
     *  通过手机号与密码校验用户
     * @param phonePasswordVerifyForm phonePasswordVerifyForm
     * @return Result<UserVerifyQuery>
     */
    @PostMapping("/phone")
    public Result<UserVerifyQuery> phonePasswordVerify(@RequestBody PhonePasswordVerifyForm phonePasswordVerifyForm) {
        // 保证信息不为空
        if (phonePasswordVerifyForm.getPhone().isEmpty() || phonePasswordVerifyForm.getPassword().isEmpty()) {
            return Result.ok(new UserVerifyQuery(null, null, VerifyInfoType.USER_NOT_EXIST.getType()));
        }

        return Result.ok(userInfoService.phonePasswordVerify(phonePasswordVerifyForm));
    }

    /**
     *  通过邮箱与密码校验用户
     * @param emailPasswordVerifyForm emailPasswordVerifyForm
     * @return Result<UserVerifyQuery>
     */
    @PostMapping("/email")
    public Result<UserVerifyQuery> emailPasswordVerify(@RequestBody EmailPasswordVerifyForm emailPasswordVerifyForm) {
        if (emailPasswordVerifyForm.getEmail().isEmpty() || emailPasswordVerifyForm.getPassword().isEmpty()) {
            return Result.ok(new UserVerifyQuery(null, null, VerifyInfoType.USER_NOT_EXIST.getType()));
        }


        return Result.ok(userInfoService.emailPasswordVerify(emailPasswordVerifyForm));
    }
}
