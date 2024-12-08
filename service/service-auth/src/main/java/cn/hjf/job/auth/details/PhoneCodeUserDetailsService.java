package cn.hjf.job.auth.details;

import cn.hjf.job.auth.config.KeyProperties;
import cn.hjf.job.auth.service.UserRoleService;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.dto.user.UserInfoStatus;
import cn.hjf.job.user.client.UserInfoFeignClient;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class PhoneCodeUserDetailsService implements UserDetailsService {

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
     * 手机号验证码方式获取用户信息
     *
     * @param phone    手机号
     * @param userType 用户类型
     * @return UserDetails
     */
    public UserDetails loadUserByUsernameAndType(String phone, Integer userType) throws UsernameNotFoundException {
        // 获取用户信息
        Result<UserInfoStatus> userInfoStatusResult = getUserInfoStatusByPhone(phone, userType);

        // 校验用户信息
        UserInfoStatus userInfoStatus = userInfoStatusResult.getData();
        validateUserInfo(userInfoStatus);

        // 获取用户角色
        List<String> userRoles = getUserRoles(userInfoStatus.getId());

        // 构造权限
        List<GrantedAuthority> authorities = buildAuthorities(userRoles);

        return new User(userInfoStatus.getId().toString(), "", authorities);
    }

    /**
     * 获取用户信息（手机号验证码方式）
     */
    private Result<UserInfoStatus> getUserInfoStatusByPhone(String phone, Integer userType) {
        Result<UserInfoStatus> result = userInfoFeignClient.getUserInfoStatusByPhoneCode(phone, userType, keyProperties.getKey());
        if (result.getCode() != 200 || result.getData() == null) {
            throw new UsernameNotFoundException("登录失败，未找到用户信息");
        }
        return result;
    }

    /**
     * 校验用户信息
     */
    private void validateUserInfo(UserInfoStatus userInfoStatus) {
        if (userInfoStatus == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (userInfoStatus.getStatus() == 2) {
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
