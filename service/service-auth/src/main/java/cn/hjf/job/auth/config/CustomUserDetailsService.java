package cn.hjf.job.auth.config;

import cn.hjf.job.auth.service.UserRoleService;
import cn.hjf.job.common.constant.AccountStateConstant;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.query.user.UserVerifyQuery;
import cn.hjf.job.user.client.UserInfoFeignClient;
import jakarta.annotation.Resource;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private UserInfoFeignClient userInfoFeignClient;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        //获取密码
        String password;
        Result<String> result = userInfoFeignClient.getPasswordById(Long.parseLong(id));
        if (result == null) throw new UsernameNotFoundException("用户名或密码错误");
        password = result.getData();
        List<String> userRoles = userRoleService.getUserRoleById(Long.parseLong(id));
        List<GrantedAuthority> authorities = new ArrayList<>(userRoles.size());
        userRoles.forEach(userRole -> authorities.add(new SimpleGrantedAuthority(userRole)));
        // TODO 查询全部权限

        return new User(id, password, authorities);
    }
}
