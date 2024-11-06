package cn.hjf.job.gateway.config;


import cn.hjf.job.common.result.Result;
import cn.hjf.job.common.result.ResultCodeEnum;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * @author hjf
 * @version 1.0
 * @description 网关认证过滤器
 */

@Component
public class GatewayAuthFilter implements GlobalFilter {

    @Resource
    private JwtDecoder jwtDecoder;

    @Resource
    private WhitelistConfig whitelistConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUrl = exchange.getRequest().getPath().value();

        // 放行白名单path
        if (isWhiteListed(requestUrl)) {
            return chain.filter(exchange);
        }

        // 获取token
        String token = getToken(exchange);

        //检查token是否存在
        if (token == null || token.isEmpty()) {
            return buildReturnMono("没有认证", exchange);
        }

        // 判断是否是有效的token
        try {
            Jwt jwt = jwtDecoder.decode(token);
            // 你可以在这里添加更多的自定义验证逻辑，比如检查 JWT 的过期时间等
            Instant expiration = jwt.getExpiresAt();
            if (expiration.isBefore(Instant.now())) {
                return buildReturnMono("认证令牌已过期", exchange);
            }
            return chain.filter(exchange);
        } catch (JwtException e) {
            return buildReturnMono("认证令牌无效", exchange);
        }
    }

    /**
     * @param exchange ServerWebExchange
     * @return token
     */
    private String getToken(ServerWebExchange exchange) {
        // 获取token  Authorization中获取
        String header = exchange.getRequest().getHeaders().getFirst("Authorization"); //
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }


    private Mono<Void> buildReturnMono(String error, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        String jsonString = JSON.toJSONString(Result.build(error, ResultCodeEnum.PERMISSION));
        byte[] bits = jsonString.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * @param requestUrl 请去路径
     * @return boolean
     */
    public boolean isWhiteListed(String requestUrl) {
        for (String whiteListedUrl : whitelistConfig.getPath()) {
            if (matchesWithWildcard(requestUrl, whiteListedUrl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param requestUrl     请去路径
     * @param whiteListedUrl 白名单路径
     * @return boolean
     */
    public static boolean matchesWithWildcard(String requestUrl, String whiteListedUrl) {
        // 将通配符 * 转换为正则表达式的 .*
        String regex = whiteListedUrl.replaceAll("\\*", ".*");
        return requestUrl.matches(regex);
    }
}
