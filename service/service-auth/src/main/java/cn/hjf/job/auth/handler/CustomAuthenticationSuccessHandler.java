package cn.hjf.job.auth.handler;

import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.jwt.JwtUtil;
import cn.hjf.job.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 生成token 携带用户名以及角色
        // 获取角色
        List<String> roles = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        // 生成 token
        String token = jwtUtil.generateToken(authentication.getName(), roles);

        // 将 token 存储在redis中
        redisTemplate.opsForValue().set(
                RedisConstant.USER_TOKEN + authentication.getName(),
                token,
                RedisConstant.USER_TOKEN_TIME_OUT, // 过期时间一天
                TimeUnit.SECONDS
        );

        // TODO 将权限信息存储到redis中

        // 设置响应数据、设置响应类型为 JSON
        Result<String> result = Result.ok(token);
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = new ObjectMapper().writeValueAsString(result);
        response.getWriter().write(jsonResponse);
    }
}
