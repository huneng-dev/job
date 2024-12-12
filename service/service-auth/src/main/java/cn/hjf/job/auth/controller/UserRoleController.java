package cn.hjf.job.auth.controller;

import cn.hjf.job.auth.config.KeyProperties;
import cn.hjf.job.auth.service.UserRoleService;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.request.auth.DefaultUserRoleRequest;
import cn.hjf.job.model.request.auth.UserRoleRequest;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户角色控制器
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@RestController
@RequestMapping("/role")
public class UserRoleController {

    @Resource
    private KeyProperties keyProperties;

    @Resource(name = "userRoleServiceImpl")
    private UserRoleService userRoleService;

    /**
     * 设置用户默认角色
     * tip: 只能在用户初始化阶段调用 (如:注册)
     *
     * @param defaultUserRoleRequest 用户ID和角色ID
     * @return Result<String>
     */
    @PostMapping("/default")
    public Result<String> setDefaultUserRole(@RequestBody DefaultUserRoleRequest defaultUserRoleRequest) {
        // 判断是否有权限
        String key = getKey();
        if (key == null || !key.equals(defaultUserRoleRequest.getKey())) {
            return Result.fail();
        }

        // 设置权限
        boolean isSuccess = userRoleService.setDefaultUserRole(defaultUserRoleRequest.getUserId(), defaultUserRoleRequest.getRoles());
        if (isSuccess) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 设置用户权限
     *
     * @param userRoleRequest 用户权限
     * @return 是否成功
     */
    @PostMapping("/user")
    public Result<Boolean> setUserRole(@RequestBody UserRoleRequest userRoleRequest) {
        // 判断是否有权限
        String key = getKey();
        if (key == null || !key.equals(userRoleRequest.getKey())) {
            return Result.fail();
        }

        boolean isSuccess = userRoleService.setUserRole(userRoleRequest.getUserId(), userRoleRequest.getRole());
        return isSuccess ? Result.ok(true) : Result.ok(false);
    }

    private String getKey() {
        return keyProperties.getKey();
    }

}
