package cn.hjf.job.auth.config;


import cn.hjf.job.common.constant.LoginMethodConstant;
import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.jwt.JwtUtil;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.form.auth.LoginInfoForm;
import cn.hjf.job.user.client.UserInfoFeignClient;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final RedisTemplate<String, Object> redisTemplate;

    private final Long expiration;

    private final UserInfoFeignClient userInfoFeignClient;

    private final JwtUtil jwtUtil;

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, RedisTemplate<String, Object> redisTemplate, Long expiration, UserInfoFeignClient userInfoFeignClient, JwtUtil jwtUtil) {
        this.expiration = expiration;
        this.redisTemplate = redisTemplate;
        this.userInfoFeignClient = userInfoFeignClient;
        this.jwtUtil = jwtUtil;
        super.setAuthenticationManager(authenticationManager);
        super.setPostOnly(true);
        super.setFilterProcessesUrl("/auth/login");
        super.setUsernameParameter("id");
        super.setPasswordParameter("password");
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("CustomUsernamePasswordAuthenticationFilter authentication start");
        // 数据是通过 RequestBody 传输
        LoginInfoForm user = JSON.parseObject(request.getInputStream(), StandardCharsets.UTF_8, LoginInfoForm.class);

        // 判断登录类型 根据不同类型查询用 用户id
        Long id;
        if (user.getLoginMethod().equals(LoginMethodConstant.PHONE)) {  // 根据手机号查询用户id
            Result<Long> userIdByPhone = userInfoFeignClient.findUserIdByPhone(user.getPhone());
            id = userIdByPhone.getData();
        } else if (user.getLoginMethod().equals(LoginMethodConstant.EMAIL)) { // 根据手机号查询用户id
            Result<Long> userIdByEmail = userInfoFeignClient.findUserIdByEmail(user.getEmail());
            id = userIdByEmail.getData();
        } else {
            throw new UsernameNotFoundException("登录方式异常");
        }
        // 判断用户是否存在
        if (id == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(id.toString(), user.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException, java.io.IOException {
        // 生成token 携带用户名以及权限（角色）
        // 获取权限（角色）
        List<String> roles = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        // TODO 将角色与权限分离，角色保存到JWT、Redis，权限保存到 Redis
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        // 生成 token
        String token = jwtUtil.generateToken(authResult.getName(), roles);

        // 将 token 存储在redis中
        redisTemplate.opsForValue().set(
                RedisConstant.USER_TOKEN + authResult.getName(),
                token,
                RedisConstant.USER_TOKEN_TIME_OUT, // 过期时间一天
                TimeUnit.SECONDS
        );

        // 将 权限 信息存储到redis中

        Result<String> result = Result.ok(token);
        // 设置响应类型为 JSON
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = new ObjectMapper().writeValueAsString(result);
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException, java.io.IOException {
        Result<String> build = Result.build("", 403, failed.getMessage());
        // 设置响应类型为 JSON
        response.setContentType("application/json;charset=UTF-8");

        // 将对象转换为 JSON 字符串
        String jsonResponse = new ObjectMapper().writeValueAsString(build);
        response.getWriter().write(jsonResponse);
    }


}
