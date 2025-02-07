package cn.hjf.job.push.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 此配置类用于 Stomp 中继代理
 */
@Data
@Component
@ConfigurationProperties(prefix = "job.stomp")
public class StompProperties {

    private String host;

    private Integer port;

    private String login;

    private String password;

    // 虚拟主机
    private String vHost;

}
