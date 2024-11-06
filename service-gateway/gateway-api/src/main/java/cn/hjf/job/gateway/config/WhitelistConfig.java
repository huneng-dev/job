package cn.hjf.job.gateway.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author hjf
 * @version 1.0
 * @description 白名单加载类
 */
@Getter
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "whitelist")
public class WhitelistConfig {

    // 白名单
    private Set<String> path = new HashSet<>();

}
