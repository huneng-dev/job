package cn.hjf.job.auth.provider;

import cn.hjf.job.auth.details.PhoneCodeUserDetailsService;
import cn.hjf.job.auth.token.PhoneCodeAuthenticationToken;
import cn.hjf.job.common.constant.RedisConstant;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class PhoneCodeAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private PhoneCodeUserDetailsService phoneCodeUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PhoneCodeAuthenticationToken phoneCodeAuthenticationToken = (PhoneCodeAuthenticationToken) authentication;
        String phone = phoneCodeAuthenticationToken.getPhone();
        String code = phoneCodeAuthenticationToken.getCode();
        Integer type = phoneCodeAuthenticationToken.getType();

        // 获取邮箱登录验证码
        String key = RedisConstant.PHONE_LOGIN_CODE + phone;
        String rawCode = redisTemplate.opsForValue().get(key);

        // 校验验证码是否正确
        if (rawCode == null) {
            throw new BadCredentialsException("验证码已失效");
        }

        // 如果验证码不匹配，表示用户输入的验证码错误
        if (!rawCode.equals(code)) {
            throw new BadCredentialsException("验证码错误");
        }

        // 删除 redis 中的验证码
        redisTemplate.delete(key);
        // **************认证成功获取用户信息********************
        UserDetails userDetails = phoneCodeUserDetailsService.loadUserByUsernameAndType(phone, type);

        return new PhoneCodeAuthenticationToken(
                userDetails.getUsername(),
                code,
                type,
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PhoneCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
