package cn.hjf.job.user.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.form.user.EmailRegisterInfoForm;
import cn.hjf.job.model.form.user.PhoneRegisterInfoForm;
import cn.hjf.job.model.query.user.UserInfoPasswordStatus;
import cn.hjf.job.model.query.user.UserInfoQuery;
import cn.hjf.job.model.query.user.UserInfoStatus;
import cn.hjf.job.model.request.user.EmailAndUserTypeRequest;
import cn.hjf.job.model.request.user.PhoneAndUserTypeRequest;
import cn.hjf.job.model.vo.user.UserInfoVo;
import cn.hjf.job.user.config.KeyProperties;
import cn.hjf.job.user.exception.EmailAlreadyRegisteredException;
import cn.hjf.job.user.exception.PhoneAlreadyRegisterException;
import cn.hjf.job.user.exception.VerificationCodeException;
import cn.hjf.job.user.service.UserInfoService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * 用户信息
 *
 * @author hjf
 * @since 2024-10-31
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private KeyProperties keyProperties;

    /**
     * 获取用户信息
     *
     * @return UserInfoQuery
     */
    @GetMapping("/info")
    public Result<UserInfoQuery> getUserInfo(Principal principal) {
        if (principal == null) {
            return Result.fail();
        }
        // 获取用户信息
        UserInfoVo userInfoVo = userInfoService.getUserInfo(Long.valueOf(principal.getName()));
        UserInfoQuery userInfoQuery = new UserInfoQuery();
        if (userInfoVo != null) BeanUtils.copyProperties(userInfoVo, userInfoQuery);
        return Result.ok(userInfoQuery);
    }

    /**
     * 招聘端邮箱注册
     *
     * @param emailRegisterInfoForm 注册表单
     * @return 返回注册信息
     */
    @PostMapping("/recruiter/register/email")
    public Result<String> recruiterRegisterByEmail(@RequestBody EmailRegisterInfoForm emailRegisterInfoForm
    ) {
        try {
            // 调用 service 层注册方法
            boolean isRegistered = userInfoService.recruiterRegisterByEmail(emailRegisterInfoForm);

            // 判断是否注册成功
            if (isRegistered) {
                return Result.ok("邮箱注册成功");
            } else {
                return Result.fail("邮箱注册失败");
            }

        } catch (VerificationCodeException e) {
            // 验证码相关异常处理
            return Result.fail(e.getMessage());

        } catch (EmailAlreadyRegisteredException e) {
            // 邮箱已注册异常处理
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并记录日志
            log.error("注册异常: ", e);
            return Result.fail("系统错误，请稍后再试");
        }
    }

    /**
     * 招聘端手机号注册
     *
     * @param phoneRegisterInfoForm 注册表单
     * @return 返回注册信息
     */
    @PostMapping("/recruiter/register/phone")
    public Result<String> recruiterRegisterByPhone(@Valid @RequestBody PhoneRegisterInfoForm phoneRegisterInfoForm) {
        try {

            boolean isRegistered = userInfoService.recruiterRegisterByPhone(phoneRegisterInfoForm);
            // 判断是否注册成功
            if (isRegistered) {
                return Result.ok("手机注册成功");
            } else {
                return Result.fail("手机注册失败");
            }
        } catch (VerificationCodeException e) {
            // 验证码相关异常处理
            return Result.fail(e.getMessage());

        } catch (PhoneAlreadyRegisterException e) {
            // 邮箱已注册异常处理
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并记录日志
            log.error("注册异常: ", e);
            return Result.fail("系统错误，请稍后再试");
        }
    }

    /**
     * 应聘端手机号注册
     *
     * @param phoneRegisterInfoForm 手机注册表单
     * @return 返回注册信息
     */
    @PostMapping("/candidate/register/phone")
    public Result<String> CandidateRegisterByPhone(@Valid @RequestBody PhoneRegisterInfoForm phoneRegisterInfoForm) {
        try {

            boolean isRegistered = userInfoService.recruiterRegisterByPhone(phoneRegisterInfoForm);
            // 判断是否注册成功
            if (isRegistered) {
                return Result.ok("手机注册成功");
            } else {
                return Result.fail("手机注册失败");
            }
        } catch (VerificationCodeException e) {
            // 验证码相关异常处理
            return Result.fail(e.getMessage());

        } catch (PhoneAlreadyRegisterException e) {
            // 邮箱已注册异常处理
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并记录日志
            log.error("注册异常: ", e);
            return Result.fail("系统错误，请稍后再试");
        }
    }

    /**
     * 邮件验证码方式获取用户信息
     *
     * @param email    邮箱
     * @param userType 用户类型
     * @param key      密钥
     * @return UserInfoStatus
     */
    @GetMapping("/email-code")
    public Result<UserInfoStatus> getUserInfoStatusByEmailCode(
            @RequestParam String email,
            @RequestParam Integer userType,
            @RequestParam String key
    ) {
        // 验证密钥
        if (key == null || !key.equals(keyProperties.getKey())) {
            return Result.build(null, 200, "无权访问");
        }
        EmailAndUserTypeRequest emailAndUserTypeRequest = new EmailAndUserTypeRequest(email, userType, "");

        return Result.ok(userInfoService.getUserInfoStatusByEmailCode(emailAndUserTypeRequest));
    }

    /**
     * 手机验证码方式获取用户信息
     *
     * @param phone    手机号
     * @param userType 用户类型
     * @param key      密钥
     * @return UserInfoStatus
     */
    @GetMapping("/phone-code")
    public Result<UserInfoStatus> getUserInfoStatusByPhoneCode(
            @RequestParam String phone,
            @RequestParam Integer userType,
            @RequestParam String key
    ) {
        // 验证密钥
        if (key == null || !key.equals(keyProperties.getKey())) {
            return Result.build(null, 200, "无权访问");
        }
        PhoneAndUserTypeRequest phoneAndUserTypeRequest = new PhoneAndUserTypeRequest(phone, userType, "");
        return Result.ok(userInfoService.getUserInfoStatusByPhoneCode(phoneAndUserTypeRequest));
    }

    /**
     * 邮箱密码方式获取用户信息
     *
     * @param email    邮箱
     * @param userType 用户类型
     * @param key      密钥
     * @return UserInfoPasswordStatus
     */
    @GetMapping("/email-password")
    public Result<UserInfoPasswordStatus> getUserInfoPasswordStatusByEmailPassword(
            @RequestParam String email,
            @RequestParam Integer userType,
            @RequestParam String key
    ) {
        // 验证密钥
        if (key == null || !key.equals(keyProperties.getKey())) {
            return Result.build(null, 200, "无权访问");
        }
        EmailAndUserTypeRequest emailAndUserTypeRequest = new EmailAndUserTypeRequest(email, userType, "");
        return Result.ok(userInfoService.getUserInfoPasswordStatusByEmailPassword(emailAndUserTypeRequest));
    }


    /**
     * 手机号密码方式获取用户信息
     *
     * @param phone    手机号
     * @param userType 用户类型
     * @param key      密钥
     * @return UserInfoPasswordStatus
     */
    @GetMapping("/phone-password")
    public Result<UserInfoPasswordStatus> getUserInfoPasswordStatusByPhonePassword(
            @RequestParam String phone,
            @RequestParam Integer userType,
            @RequestParam String key
    ) {
        // 验证密钥
        if (key == null || !key.equals(keyProperties.getKey())) {
            return Result.build(null, 200, "无权访问");
        }
        PhoneAndUserTypeRequest phoneAndUserTypeRequest = new PhoneAndUserTypeRequest(phone, userType, "");
        return Result.ok(userInfoService.getUserInfoPasswordStatusByPhonePassword(phoneAndUserTypeRequest));
    }
}
