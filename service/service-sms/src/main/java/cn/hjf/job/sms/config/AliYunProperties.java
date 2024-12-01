package cn.hjf.job.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun")
public class AliYunProperties {

    private String accessKeyId;  // 阿里云 AccessKey ID

    private String accessKeySecret;  // 阿里云 AccessKey Secret
}
