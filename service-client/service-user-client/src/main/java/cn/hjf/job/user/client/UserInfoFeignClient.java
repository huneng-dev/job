package cn.hjf.job.user.client;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.dto.user.UserInfoPasswordStatus;
import cn.hjf.job.model.dto.user.UserInfoStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "service-user")
public interface UserInfoFeignClient {

    /**
     * 邮件验证码方式获取用户信息
     *
     * @param email    邮箱
     * @param userType 用户类型
     * @param key      密钥
     * @return UserInfoStatus
     */
    @GetMapping("/user/email-code")
    public Result<UserInfoStatus> getUserInfoStatusByEmailCode(
            @RequestParam String email,
            @RequestParam Integer userType,
            @RequestParam String key
    );

    /**
     * 手机验证码方式获取用户信息
     *
     * @param phone    手机号
     * @param userType 用户类型
     * @param key      密钥
     * @return UserInfoStatus
     */
    @GetMapping("/user/phone-code")
    public Result<UserInfoStatus> getUserInfoStatusByPhoneCode(
            @RequestParam String phone,
            @RequestParam Integer userType,
            @RequestParam String key
    );

    /**
     * 邮箱密码方式获取用户信息
     *
     * @param email    邮箱
     * @param userType 用户类型
     * @param key      密钥
     * @return UserInfoPasswordStatus
     */
    @GetMapping("/user/email-password")
    public Result<UserInfoPasswordStatus> getUserInfoPasswordStatusByEmailPassword(
            @RequestParam String email,
            @RequestParam Integer userType,
            @RequestParam String key
    );

    /**
     * 手机号密码方式获取用户信息
     *
     * @param phone    手机号
     * @param userType 用户类型
     * @param key      密钥
     * @return UserInfoPasswordStatus
     */
    @GetMapping("/user/phone-password")
    public Result<UserInfoPasswordStatus> getUserInfoPasswordStatusByPhonePassword(
            @RequestParam String phone,
            @RequestParam Integer userType,
            @RequestParam String key
    );
}
