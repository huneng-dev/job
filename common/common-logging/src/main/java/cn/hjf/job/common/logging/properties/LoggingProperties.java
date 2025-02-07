package cn.hjf.job.common.logging.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "logging.custom")
public class LoggingProperties {

    /**
     * 是否启用自定义日志
     */
    private boolean enabled = true;

    /**
     * 是否记录请求参数
     */
    private boolean logRequestParams = true;

    /**
     * 是否记录响应结果
     */
    private boolean logResponse = true;

    /**
     * 是否记录堆栈信息
     */
    private boolean includeStackTrace = true;
}
