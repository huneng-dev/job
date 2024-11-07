package cn.hjf.job.auth.service.impl;

import cn.hjf.job.auth.mapper.UserRoleMapper;
import cn.hjf.job.auth.service.UserRoleService;
import cn.hjf.job.model.entity.auth.UserRole;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-24
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<String> getUserRoleById(Long id) {
        return userRoleMapper.getUserRoleById(id);
    }
}
