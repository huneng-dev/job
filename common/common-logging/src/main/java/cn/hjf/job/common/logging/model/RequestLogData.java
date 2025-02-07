package cn.hjf.job.common.logging.model;


import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class RequestLogData {
    // 基本请求信息
    private String url;                       // 请求URL
    private String method;                    // 请求方法

    // IP信息
    private String clientIp;                  // 客户端IP
    private String xForwardedFor;            // X-Forwarded-For 头信息
    private String xRealIp;                  // X-Real-IP 头信息
    private String remoteHost;               // 远程主机名
    private String remoteAddr;               // 远程地址
    private int remotePort;                  // 远程端口

    // 性能信息
    private Long duration;                    // 请求处理时长
    private Long requestTime;                // 请求开始时间
    private Long responseTime;               // 响应时间

    // 响应信息
    private int responseStatus;              // 响应状态码
}
