package cn.hjf.job.auth.details;

import cn.hjf.job.auth.config.KeyProperties;
import cn.hjf.job.auth.service.UserRoleService;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.query.user.UserInfoPasswordStatus;
import cn.hjf.job.user.client.UserInfoFeignClient;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmailPasswordUserDetailsService implements UserDetailsService {

    @Resource
    private UserInfoFeignClient userInfoFeignClient;

    @Resource
    private KeyProperties keyProperties;

    @Resource
    private UserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    /**
     * 加载用户数据
     *
     * @param email    邮箱
     * @param userType 用户类型
     * @return UserDetails
     */
    public UserDetails loadUserByEmailAndUserType(String email, Integer userType) throws UsernameNotFoundException {
        // 获取用户信息
        Result<UserInfoPasswordStatus> result = getUserInfoPasswordStatusByEmailPassword(email, userType);

        // 校验用户信息
        UserInfoPasswordStatus data = result.getData();
        validateUserInfo(data);

        // 获取用户角色
        List<String> userRoles = getUserRoles(data.getId());

        // 构造权限
        List<GrantedAuthority> authorities = buildAuthorities(userRoles);

        return new User(
                data.getId().toString(),
                data.getPassword(),
                authorities
        );
    }

    /**
     * 获取用户信息（邮箱密码方式）
     */
    private Result<UserInfoPasswordStatus> getUserInfoPasswordStatusByEmailPassword(String email, Integer userType) {
        Result<UserInfoPasswordStatus> result = userInfoFeignClient.getUserInfoPasswordStatusByEmailPassword(email, userType, keyProperties.getKey());
        if (result.getCode() != 200 || result.getData() == null) {
            throw new UsernameNotFoundException("登录失败，未找到用户信息");
        }
        return result;
    }

    /**
     * 校验用户信息
     */
    private void validateUserInfo(UserInfoPasswordStatus userInfoPasswordStatus) {
        if (userInfoPasswordStatus == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (userInfoPasswordStatus.getStatus() == 2) {
            throw new UsernameNotFoundException("用户已禁用");
        }
    }

    /**
     * 获取用户角色
     */
    private List<String> getUserRoles(Long userId) {
        return userRoleService.getUserRoleById(userId);
    }

    /**
     * 构建用户权限
     */
    private List<GrantedAuthority> buildAuthorities(List<String> userRoles) {
        return userRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
