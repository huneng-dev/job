package cn.hjf.job.auth.service.impl;

import cn.hjf.job.auth.mapper.UserRoleMapper;
import cn.hjf.job.auth.service.UserRoleService;
import cn.hjf.job.model.entity.auth.UserRole;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-24
 */
@Service
@Slf4j
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<String> getUserRoleById(Long id) {
        return userRoleMapper.getUserRoleById(id);
    }

    @Override
    public boolean setDefaultUserRole(Long id, List<Long> roles) {
        // 批量插入用户角色
        try {
            List<UserRole> userRoles = roles.stream()
                    .map(role -> new UserRole(id, role))
                    .collect(Collectors.toList());

            userRoleMapper.insert(userRoles);
            return true;
        } catch (Exception e) {
            log.error("Error 用户角色插入失败: {}", id, e);
            return false;
        }
    }

    @Override
    public boolean setUserRole(Long id, Long roleId) {
        UserRole userRole = new UserRole(id, roleId);
        int num = userRoleMapper.insert(userRole);
        return num == 1;
    }
}
