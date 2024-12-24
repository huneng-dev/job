package cn.hjf.job.common.es.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ESProperties {

    @Value("${es.hostAndPort}")
    private String hostAndPort;

    @Value("${es.username}")
    private String username;

    @Value("${es.password}")
    private String password;
}
