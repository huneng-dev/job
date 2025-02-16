package cn.hjf.job.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "coturn.auth")
public class CoturnAuthProperties {


    private String secret;

}
