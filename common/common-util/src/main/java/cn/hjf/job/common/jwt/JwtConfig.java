package cn.hjf.job.common.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author hjf
 * @version 1.0
 * @description 加载对称加密的 signingKey
 */

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private  String signingKey;

    // 将 String 转换为 SecretKey
    public SecretKey getSigningKey() {
        byte[] keyBytes = signingKey.getBytes(); // 转换为字节数组
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }


}
