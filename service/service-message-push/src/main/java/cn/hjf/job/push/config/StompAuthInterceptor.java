package cn.hjf.job.push.config;

import cn.hjf.job.common.constant.RedisConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


/**
 * Stomp 授权连接拦截器
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@Component
@Slf4j
public class StompAuthInterceptor implements ChannelInterceptor {


    @Resource
    private JwtDecoder jwtDecoder;

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        // 首次连接鉴权
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 尝试获取 token
            List<String> nativeHeader = accessor.getNativeHeader("Authorization");
            if (nativeHeader != null && nativeHeader.get(0) != null) {
                // 获取 token
                String token = nativeHeader.get(0);
                // 解析出 Jwt
                String jwt = getJwt(token);
                // 获取不到 Jwt 拒绝连接
                if (jwt == null) return null;
                // 判断 Jwt 是否合法 (格式、是否过期)
                Jwt rowJwt = checkJwt(jwt);
                // Jwt 被篡改或过期 拒绝连接
                if (rowJwt == null) return null;

                // 获取用户名
                String username = rowJwt.getClaim("sub");

                // 提取角色信息
                List<GrantedAuthority> authorities = extractAuthoritiesFromJwt(rowJwt);

                // 设置授权信息方便以后鉴权
                accessor.setUser(new UsernamePasswordAuthenticationToken(username, null, authorities));
            }
        }

        return message;
    }


    private String getJwt(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }


    private Jwt checkJwt(String jwt) {
        Jwt rowJwt = jwtDecoder.decode(jwt);
        // 判断是否过期
        Instant expiration = rowJwt.getExpiresAt();
        if (expiration == null || expiration.isBefore(Instant.now())) {
            return null; // 过期返回 null
        }

        // 获取用户id
        String id = rowJwt.getClaim("sub");

        // 从redis中查询用户token
        String redisToken = redisTemplate.opsForValue().get(RedisConstant.USER_TOKEN + id);

        // 判断用户携带的token是否与redis中的相同
        if (!jwt.equals(redisToken)) {
            return null;
        }

        return rowJwt;
    }


    private List<GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
        // 假设角色存储在JWT的"roles"字段中
        List<String> roles = jwt.getClaim("roles");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
