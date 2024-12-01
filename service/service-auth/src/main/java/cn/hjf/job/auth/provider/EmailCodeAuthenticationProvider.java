package cn.hjf.job.auth.provider;

import cn.hjf.job.auth.details.EmailCodeUserDetailsService;
import cn.hjf.job.auth.token.EmailCodeAuthenticationToken;
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
public class EmailCodeAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private EmailCodeUserDetailsService emailCodeUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EmailCodeAuthenticationToken emailCodeAuthenticationToken = (EmailCodeAuthenticationToken) authentication;
        String email = emailCodeAuthenticationToken.getEmail();
        String code = emailCodeAuthenticationToken.getCode();
        Integer type = emailCodeAuthenticationToken.getType();

        // 获取邮箱登录验证码
        String key = RedisConstant.EMAIL_LOGIN_CODE + email;
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
        UserDetails userDetails = emailCodeUserDetailsService.loadUserByUsernameAndType(email, type);

        return new EmailCodeAuthenticationToken(
                userDetails.getUsername(),
                code,
                type,
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
