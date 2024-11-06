package cn.hjf.job.user.client;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.auth.test.UserInfoEntity;
import cn.hjf.job.model.auth.test.UserInfoForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "service-user")
public interface UserInfoFeignClient {

    /**
     * 验证密码服务
     *
     * @param userInfoForm 基本信息
     * @return 用户信息
     */
    @PostMapping("/user/verifyUser")
    public Result<UserInfoEntity> verifyUserInfo(@RequestBody UserInfoForm userInfoForm);

    /**
     * 根据手机号获取用户id
     *
     * @param phone 手机号
     * @return 用户id
     */
    @GetMapping("/user/phone/{phone}")
    public Result<Long> findUserIdByPhone(@PathVariable(name = "phone") String phone);


    /**
     * 根据邮箱获取用户id
     * @param email 邮箱
     * @return 用户id
     */
    @GetMapping("/user/email/{email}")
    public Result<Long> findUserIdByEmail(@PathVariable(name = "email") String email);


    /**
     *  查询用户的密码
     * @param id 用户id
     * @return 用户密码
     */
    @GetMapping("/user/password/{id}")
    public Result<String> getPasswordById(@PathVariable(name = "id") Long id);


}
