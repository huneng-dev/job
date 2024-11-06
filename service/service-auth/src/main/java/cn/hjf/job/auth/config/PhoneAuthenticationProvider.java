package cn.hjf.job.auth.config;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.form.user.PhonePasswordVerifyForm;
import cn.hjf.job.model.query.user.UserVerifyQuery;
import cn.hjf.job.user.client.PasswordVerifyFeignClient;
import jakarta.annotation.Resource;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 手机号登录 authenticationManager
 */
public class PhoneAuthenticationProvider implements AuthenticationProvider {

    @Setter
    private PasswordVerifyFeignClient passwordVerifyFeignClient;

    @Setter
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取登录表单信息
        String phone = authentication.getName();
        String password = (String) authentication.getCredentials();

        Result<UserVerifyQuery> userVerifyQueryResult = passwordVerifyFeignClient.phonePasswordVerify(new PhonePasswordVerifyForm(phone, password));

        if (userVerifyQueryResult == null)
            throw new BadCredentialsException("数据查询失败！请稍后重试");

        UserVerifyQuery resultData = userVerifyQueryResult.getData();

        // 密码验证通过执行后续操作
        if (resultData.getStates() == 3) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(resultData.getId().toString());
            return new UsernamePasswordAuthenticationToken(userDetails, "test",userDetails.getAuthorities());
        } else if (resultData.getStates() == 2) {
            throw new BadCredentialsException("用户已被禁用");
        } else {
            throw new BadCredentialsException("手机号或密码错误");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
