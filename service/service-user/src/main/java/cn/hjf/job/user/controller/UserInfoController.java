package cn.hjf.job.user.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.common.result.ResultCodeEnum;
import cn.hjf.job.model.auth.test.UserInfoEntity;
import cn.hjf.job.model.auth.test.UserInfoForm;
import cn.hjf.job.model.entity.user.UserInfo;
import cn.hjf.job.user.mapper.UserInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private PasswordEncoder passwordEncoder;


    /**
     * 验证密码服务
     *
     * @param userInfoForm 基本信息
     * @return 用户信息
     */
    @PostMapping("/verifyUser")
    public Result<UserInfoEntity> verifyUserInfo(@RequestBody UserInfoForm userInfoForm) {
        //保证信息不为空
        if (userInfoForm.getPhone().isEmpty() || userInfoForm.getPassword().isEmpty()) {
            //信息为空就返回用户不存在
            return Result.ok(new UserInfoEntity(null, null, 0));
        }
        // 根据信息 查询信息
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.eq(UserInfo::getPhone, userInfoForm.getPhone());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);
        //
        if (!passwordEncoder.matches(userInfoForm.getPassword(), userInfo.getPassword())) {
            return Result.ok(new UserInfoEntity(null, null, 1));
        }

        return Result.ok(new UserInfoEntity(userInfo.getId(), userInfo.getType(), 3));
    }

    /**
     * 根据手机号获取用户id
     *
     * @param phone 手机号
     * @return 用户id
     */
    @GetMapping("/phone/{phone}")
    public Result<Long> findUserIdByPhone(@PathVariable(name = "phone") String phone) {
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.select(UserInfo::getId).eq(UserInfo::getPhone, phone);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);
        return userInfo == null ? Result.ok(null) : Result.ok(userInfo.getId());
    }

    /**
     * 根据邮箱获取用户id
     * @param email 邮箱
     * @return 用户id
     */
    @GetMapping("/email/{email}")
    public Result<Long> findUserIdByEmail(@PathVariable(name = "email") String email) {
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.select(UserInfo::getId).eq(UserInfo::getEmail, email);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);
        return userInfo == null ? Result.ok(null) : Result.ok(userInfo.getId());
    }

    /**
     *  查询用户的密码
     * @param id 用户id
     * @return 用户密码
     */
    @GetMapping("/password/{id}")
    public Result<String> getPasswordById(@PathVariable(name = "id") Long id){
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.select(UserInfo::getPassword).eq(UserInfo::getId,id);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);
        return userInfo == null ? Result.ok(null) : Result.ok(userInfo.getPassword());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasAuthority('CANDIDATE')")
    public Result<UserInfo> getUserInfoById(@PathVariable("id") Long id){
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.eq(UserInfo::getId,id);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);
        return userInfo == null ? Result.build(null,ResultCodeEnum.FAIL):Result.ok(userInfo);
    }
}
