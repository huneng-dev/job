package cn.hjf.job.auth.service.impl;


import cn.hjf.job.auth.mapper.PermissionMapper;
import cn.hjf.job.auth.service.PermissionService;
import cn.hjf.job.model.entity.auth.Permission;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
