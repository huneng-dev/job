package cn.hjf.job.common.logging.config;

import cn.hjf.job.common.logging.aspect.RequestLogAspect;
import cn.hjf.job.common.logging.properties.LoggingProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnProperty(prefix = "common.logging", name = "enabled", matchIfMissing = true)
public class LoggingAutoConfiguration {

    @Bean
    public RequestLogAspect requestLogAspect() {
        return new RequestLogAspect();
    }
}
