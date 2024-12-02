package cn.hjf.job.auth.provider;

import cn.hjf.job.auth.details.EmailPasswordUserDetailsService;
import cn.hjf.job.auth.details.PhonePasswordUserDetailsService;
import cn.hjf.job.auth.token.EmailPasswordAuthenticationToken;
import cn.hjf.job.auth.token.PhonePasswordAuthenticationToken;
import cn.hjf.job.common.constant.RedisConstant;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EmailPasswordAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private EmailPasswordUserDetailsService emailPasswordUserDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取参数
        EmailPasswordAuthenticationToken emailPasswordAuthenticationToken
                = (EmailPasswordAuthenticationToken) authentication;

        String email = emailPasswordAuthenticationToken.getEmail();
        String password = emailPasswordAuthenticationToken.getPassword();
        Integer userType = emailPasswordAuthenticationToken.getType();

        // redisKey 前缀
        String attemptKey = RedisConstant.PASSWORD_LOGIN_ATTEMPTS_PREFIX +
                RedisConstant.USER_TYPE_MAP.getOrDefault(userType, "unknown:") +
                "email:" + email;

        String lastAttemptTimeKey = attemptKey + ":lastAttemptTime";

        Integer attempts = getIntegerFromRedis(attemptKey);
        Long lastAttemptTime = getLongFromRedis(lastAttemptTimeKey);

        // 如果尝试次数超过 5 次且未超过冷却时间
        if (attempts != null && attempts >= 5 && lastAttemptTime != null) {
            long elapsedTime = System.currentTimeMillis() - lastAttemptTime;  // 已经过的时间（毫秒）
            long remainingTime = RedisConstant.LOCKED_ACCOUNT_COOL_DOWN_TIME_MS - elapsedTime;  // 剩余冷却时间（毫秒）

            if (remainingTime > 0) {
                // 计算剩余的分钟数
                long remainingMinutes = remainingTime / (60 * 1000);  // 转换为分钟
                throw new LockedException("尝试次数过多，请 " + remainingMinutes + " 分钟后重试");
            }
        }

        // 从UserDetailService中获取用户信息
        UserDetails userDetails = emailPasswordUserDetailsService.loadUserByEmailAndUserType(email, userType);
        // 比对密码
        boolean matches = passwordEncoder.matches(password, userDetails.getPassword());
        // 比对失败:
        if (!matches) {
            if (attempts == null) {
                attempts = 0;
            }
            redisTemplate.opsForValue().set(attemptKey, String.valueOf(attempts + 1));
            redisTemplate.opsForValue().set(lastAttemptTimeKey, String.valueOf(System.currentTimeMillis()));
            throw new BadCredentialsException("密码错误");
        }

        // 验证通过删除尝试记录
        if (attempts != null) {
            redisTemplate.delete(attemptKey);
            redisTemplate.delete(lastAttemptTimeKey);
        }

        // 校验通过生成 PhonePasswordAuthenticationToken
        return new PhonePasswordAuthenticationToken(
                userDetails.getUsername(),
                password,
                userType,
                userDetails.getAuthorities()
        );
    }

    // 获取 Integer 类型的值
    private Integer getIntegerFromRedis(String key) {
        String value = redisTemplate.opsForValue().get(key);
        try {
            return (value != null) ? Integer.valueOf(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // 获取 Long 类型的值
    private Long getLongFromRedis(String key) {
        String value = redisTemplate.opsForValue().get(key);
        try {
            return (value != null) ? Long.parseLong(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailPasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
