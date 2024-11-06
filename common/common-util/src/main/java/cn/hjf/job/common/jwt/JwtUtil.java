package cn.hjf.job.common.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Configuration
public class JwtUtil {

    @Resource
    private JwtConfig jwtConfig;

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(jwtConfig.getSigningKey()).build();
    }


    /**
     * 生成包含用户名和角色信息的 JWT Token
     *
     * @param username 用户id
     * @param roles    角色信息
     * @return JWT
     */
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setSubject(username)  // 设置用户名
                .claim("roles", roles) // 将角色列表作为 claim 加入到 token 中
                .setIssuedAt(new Date())  // 设置创建时间
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 设置过期时间（24小时）
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSigningKey()) // 使用 HS256 算法加密
                .compact();
    }
}
