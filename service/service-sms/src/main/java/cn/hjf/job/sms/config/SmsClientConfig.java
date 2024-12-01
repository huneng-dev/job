package cn.hjf.job.sms.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsClientConfig {

    @Resource
    private AliYunProperties aliYunProperties;
    @Resource
    private SmsCodeProperties smsCodeProperties;

    @Bean
    public Client createClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(aliYunProperties.getAccessKeyId())
                .setAccessKeySecret(aliYunProperties.getAccessKeySecret());

        config.endpoint = smsCodeProperties.getEndpoint();
        return new Client(config);
    }
}
