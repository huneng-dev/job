package cn.hjf.job.gateway.config;


import cn.hjf.job.common.jwt.JwtUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;

@SpringBootTest
public class TokenTest {

    @Resource
    private JwtUtil jwtUtil;

    @Test
    public void getJwtToken() {
        String jwtToken = jwtUtil.generateToken("123123",new ArrayList<>());
        System.out.println(jwtToken);
    }

    @Test
    public void validateToken() {
        // 需要验证的token
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjMxMjMiLCJpYXQiOjE3MzAyOTYxNTksImV4cCI6MTczMDM4MjU1OX0.W2m02AlG2iHrSaJXoe610jz7cK8tpB9HxFD37z8rQ7U";

        Jwt jwt = jwtUtil.jwtDecoder().decode(jwtToken);

        // 获取信息
        String subject = jwt.getSubject();
        Map<String, Object> claims = jwt.getClaims();
        Instant expiration = jwt.getExpiresAt();

        System.out.println("Subject: " + subject);
        System.out.println("Claims: " + claims);
        System.out.println("Expiration: " + expiration);
    }

}
