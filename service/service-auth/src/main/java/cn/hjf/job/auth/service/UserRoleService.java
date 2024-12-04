package cn.hjf.job.auth.service;

import cn.hjf.job.model.entity.auth.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-24
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 获取用户角色列表
     *
     * @param id 用户id
     * @return 角色Key
     */
    List<String> getUserRoleById(Long id);

    /**
     * 设置用户角色
     *
     * @param id    用户id
     * @param roles 角色
     * @return 是否成功
     */
    boolean setDefaultUserRole(Long id, List<Long> roles);

}
