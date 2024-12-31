package cn.hjf.job.user.controller;

import cn.hjf.job.common.minio.resolver.PublicFileUrlResolver;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.form.user.BindEmailForm;
import cn.hjf.job.model.form.user.EmailRegisterInfoForm;
import cn.hjf.job.model.form.user.PhoneRegisterInfoForm;
import cn.hjf.job.model.dto.user.UserInfoPasswordStatus;
import cn.hjf.job.model.dto.user.UserInfoQuery;
import cn.hjf.job.model.dto.user.UserInfoStatus;
import cn.hjf.job.model.form.user.UserIdCardInfoForm;
import cn.hjf.job.model.request.user.EmailAndUserTypeRequest;
import cn.hjf.job.model.request.user.PhoneAndUserTypeRequest;
import cn.hjf.job.model.vo.user.EmployeeInfoVo;
import cn.hjf.job.model.vo.user.RecruiterUserInfoVo;
import cn.hjf.job.model.vo.user.UserInfoVo;
import cn.hjf.job.user.config.KeyProperties;
import cn.hjf.job.user.service.UserInfoService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

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

    @Resource
    private PublicFileUrlResolver publicFileUrlResolver;

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
        assert userInfoVo != null;
        userInfoQuery.setAvatar(publicFileUrlResolver.resolveSingleUrl(userInfoVo.getAvatar()));
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

        } catch (Exception e) {
            // 捕获其他异常并记录日志
            log.error("注册异常: ", e);
            return Result.fail(e.getCause().getMessage());
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
        } catch (Exception e) {
            // 捕获其他异常并记录日志
            log.error("注册异常: ", e);
            return Result.fail(e.getCause().getMessage());
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

            boolean isRegistered = userInfoService.candidateRegisterByPhone(phoneRegisterInfoForm);
            // 判断是否注册成功
            if (isRegistered) {
                return Result.ok("手机注册成功");
            } else {
                return Result.fail("手机注册失败");
            }
        } catch (Exception e) {
            // 捕获其他异常并记录日志
            log.error("注册异常: ", e);
            return Result.fail(e.getCause().getMessage());
        }
    }

    /**
     * 应聘端邮箱注册
     *
     * @param emailRegisterInfoForm 邮箱注册表单
     * @return 返回注册信息
     */
    @PostMapping("/candidate/register/email")
    public Result<String> CandidateRegisterByEmail(@Valid @RequestBody EmailRegisterInfoForm emailRegisterInfoForm) {

        try {

            boolean isRegistered = userInfoService.candidateRegisterByEmail(emailRegisterInfoForm);
            // 判断是否注册成功
            if (isRegistered) {
                return Result.ok("邮箱注册成功");
            } else {
                return Result.fail("邮箱注册失败");
            }
        } catch (Exception e) {
            // 捕获其他异常并记录日志
            log.error("注册异常: ", e);
            return Result.fail(e.getCause().getMessage());
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

    /**
     * 设置用户身份证信息
     *
     * @param userIdCardInfoForm 用户身份证信息
     * @param principal          用户信息
     * @return Result<String>
     */
    @PreAuthorize("hasAnyRole('ROLE_BASE_CANDIDATE','ROLE_BASE_RECRUITER')")
    @PostMapping("/id/card")
    public Result<String> setUserIdCardInfo(@RequestBody UserIdCardInfoForm userIdCardInfoForm, Principal principal) {
        boolean isSuccess = userInfoService.setUserIdCardInfo(userIdCardInfoForm, Long.parseLong(principal.getName()));
        if (!isSuccess) {
            return Result.fail();
        }
        return Result.ok();
    }

    /**
     * 根据用户 ids 获取员工信息
     *
     * @param userIds 用户 id
     * @return Result<List < EmployeeInfoVo>>
     */
    @GetMapping("/employee/infos")
    public Result<List<EmployeeInfoVo>> findCompanyEmployeeByUserIds(@RequestParam List<Long> userIds, @RequestParam String serviceKey) {
        if (!Objects.equals(serviceKey, keyProperties.getKey())) {
            return Result.fail(null);
        }
        List<EmployeeInfoVo> companyEmployeeByUserIds = userInfoService.findCompanyEmployeeByUserIds(userIds);
        return Result.ok(companyEmployeeByUserIds);
    }


    /**
     * 获取招聘端用户信息
     *
     * @param principal 用户信息
     * @return Result<RecruiterUserInfoVo>
     */
    @GetMapping("/recruiter/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_RECRUITER','ROLE_EMPLOYEE_RECRUITER')")
    public Result<RecruiterUserInfoVo> getRecruiterUserInfo(Principal principal) {
        try {
            RecruiterUserInfoVo recruiterUserInfo = userInfoService.getRecruiterUserInfo(Long.parseLong(principal.getName()));
            return Result.ok(recruiterUserInfo);
        } catch (Exception e) {
            return Result.fail();
        }
    }


    /**
     * 保存用户头像
     *
     * @param principal 用户信息
     * @return Result<String>
     */
    @PutMapping("/avatar")
    public Result<String> saveUserAvatar(@RequestParam String avatarUrl, Principal principal) {
        try {
            boolean isSuccess = userInfoService.saveUserAvatar(Long.parseLong(principal.getName()), avatarUrl);
            return isSuccess ? Result.ok("成功") : Result.fail("失败");
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 绑定邮箱
     *
     * @param bindEmailForm 绑定邮箱表单
     * @param principal     用户信息
     * @return Result<String>
     */
    @PostMapping("/bind/email")
    public Result<String> bindEmail(@Valid @RequestBody BindEmailForm bindEmailForm, Principal principal) {
        try {
            boolean isSuccess = userInfoService.bindEmail(bindEmailForm, Long.parseLong(principal.getName()));
            return isSuccess ? Result.ok("绑定成功") : Result.fail("绑定失败");
        } catch (Exception e) {
            return Result.fail(e.getCause().getMessage());
        }
    }
}
