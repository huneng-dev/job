package cn.hjf.job.auth.mapper;

import cn.hjf.job.model.entity.auth.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Mapper接口
 *
 * @author hjf
 * @date 2024-10-24
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 查询用户的全部权限
     *
     * @param id 用户id
     * @return 用户权限列表
     */
    List<String> getUserRoleById(Long id);
}
