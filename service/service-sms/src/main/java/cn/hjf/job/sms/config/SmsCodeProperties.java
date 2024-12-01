package cn.hjf.job.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.sms.code")
public class SmsCodeProperties {

    private String endpoint;  // 短信服务的 endpoint

    private String signName;  // 短信签名

    private String templateCode;  // 短信模板代码
}
