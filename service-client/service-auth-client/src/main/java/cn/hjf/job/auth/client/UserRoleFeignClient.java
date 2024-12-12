package cn.hjf.job.auth.client;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.request.auth.DefaultUserRoleRequest;
import cn.hjf.job.model.request.auth.UserRoleRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-auth")
public interface UserRoleFeignClient {

    /**
     * 设置用户默认角色
     * tip: 只能在用户初始化阶段调用 (如:注册)
     *
     * @param defaultUserRoleRequest 用户ID和角色ID
     * @return Result<String>
     */
    @PostMapping("/role/default")
    public Result<String> setDefaultUserRole(@RequestBody DefaultUserRoleRequest defaultUserRoleRequest);

    /**
     * 设置用户权限
     *
     * @param userRoleRequest 用户权限
     * @return 是否成功
     */
    @PostMapping("/role/user")
    public Result<Boolean> setUserRole(@RequestBody UserRoleRequest userRoleRequest);
}
