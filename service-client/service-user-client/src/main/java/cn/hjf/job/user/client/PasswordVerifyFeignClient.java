package cn.hjf.job.user.client;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.form.user.EmailPasswordVerifyForm;
import cn.hjf.job.model.form.user.PhonePasswordVerifyForm;
import cn.hjf.job.model.query.user.UserVerifyQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-user")
public interface PasswordVerifyFeignClient {

    /**
     *  通过手机号与密码校验用户
     * @param phonePasswordVerifyForm phonePasswordVerifyForm
     * @return Result<UserVerifyQuery>
     */
    @PostMapping("/verify/phone")
    public Result<UserVerifyQuery> phonePasswordVerify(@RequestBody PhonePasswordVerifyForm phonePasswordVerifyForm);

    /**
     *  通过邮箱与密码校验用户
     * @param emailPasswordVerifyForm emailPasswordVerifyForm
     * @return Result<UserVerifyQuery>
     */
    @PostMapping("/verify/email")
    public Result<UserVerifyQuery> emailPasswordVerify(@RequestBody EmailPasswordVerifyForm emailPasswordVerifyForm);
}
