package cn.hjf.job.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "job.service.user")
public class KeyProperties {

    private String key;
}
