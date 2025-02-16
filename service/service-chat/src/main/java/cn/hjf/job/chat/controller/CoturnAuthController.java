package cn.hjf.job.chat.controller;

import cn.hjf.job.chat.config.CoturnAuthProperties;
import cn.hjf.job.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

/**
 * Coturn认证
 */
@RestController
@RequestMapping("/coturn")
public class CoturnAuthController {

    private static final String HMAC_ALGORITHM = "HmacSHA1";

    private static final long DEFAULT_TTL = 86400;
    private final SecretKeySpec secretKeySpec;

    // 使用 Record 类型定义不可变凭证对象
    public record TurnCredentials(String username, String credential) {
    }

    @Autowired
    public CoturnAuthController(CoturnAuthProperties coturnAuthProperties) {
        this.secretKeySpec = new SecretKeySpec(
                coturnAuthProperties.getSecret().getBytes(StandardCharsets.UTF_8),
                HMAC_ALGORITHM
        );
    }

    /**
     * 获取coturn认证凭证
     *
     * @param principal 认证信息
     * @return 认证凭证
     */
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE_RECRUITER','ROLE_USER_CANDIDATE')")
    @GetMapping("/credentials")
    public Result<TurnCredentials> getCredentials(Principal principal) {
        long timestamp = Instant.now().getEpochSecond();
        long expired = timestamp + DEFAULT_TTL;
        final String userId = validatePrincipal(principal);
        final String username = "%s:%s".formatted(expired, userId);

        return Result.ok(new TurnCredentials(username, generateHmac(username)));
    }

    private String generateHmac(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("HMAC initialization failed", e);
        }
    }

    private static SecretKeySpec createSecretKey(String secretHex) {
        byte[] keyBytes = HexFormat.of().parseHex(secretHex);
        return new SecretKeySpec(keyBytes, HMAC_ALGORITHM);
    }

    private static String validatePrincipal(Principal principal) {
        if (principal == null || principal.getName().isBlank()) {
            throw new IllegalArgumentException("Invalid principal");
        }
        return principal.getName();
    }
}