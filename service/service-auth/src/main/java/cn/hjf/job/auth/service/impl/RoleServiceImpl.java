package cn.hjf.job.auth.service.impl;


import cn.hjf.job.auth.mapper.RoleMapper;
import cn.hjf.job.auth.service.RoleService;
import cn.hjf.job.model.entity.auth.Role;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-23
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
