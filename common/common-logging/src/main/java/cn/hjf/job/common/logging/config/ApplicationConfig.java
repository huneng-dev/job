package cn.hjf.job.common.logging.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.ResourceBundle;

@Configuration
public class ApplicationConfig {
    private static String serviceName;

    @PostConstruct
    public void init() {
        // 应用启动时初始化一次
        serviceName = readServiceName();
    }

    public static String getServiceName() {
        return serviceName != null ? serviceName : "unknown-service";
    }

    private String readServiceName() {
        try {
            return ResourceBundle.getBundle("bootstrap")
                    .getString("spring.application.name");
        } catch (Exception e) {
            return "unknown-service";
        }
    }
}
