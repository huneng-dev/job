package cn.hjf.job.common.logging.aspect;

import cn.hjf.job.common.logging.core.LogUtils;
import cn.hjf.job.common.logging.model.RequestLogData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class RequestLogAspect {
    // 预定义IP头信息数组，避免重复创建
    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "REMOTE_ADDR"
    };

    // 常用字符串常量化
    private static final String UNKNOWN = "unknown";
    private static final String COMMA = ",";

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object logRequest(ProceedingJoinPoint point) throws Throwable {
        // 检查是否启用了INFO级别日志
        if (!log.isInfoEnabled()) {
            return point.proceed();
        }

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return point.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        long startTime = System.currentTimeMillis();

        // 预先获取可能耗时的操作结果
        String clientIp = getClientIp(request);

        // 构建基础日志数据
        RequestLogData.RequestLogDataBuilder logDataBuilder = RequestLogData.builder()
                .url(request.getRequestURL().toString())
                .method(request.getMethod())
                .requestTime(startTime)
                .clientIp(clientIp)
                .xForwardedFor(request.getHeader("X-Forwarded-For"))
                .xRealIp(request.getHeader("X-Real-IP"))
                .remoteHost(request.getRemoteHost())
                .remoteAddr(request.getRemoteAddr())
                .remotePort(request.getRemotePort());
        try {
            // 执行实际方法
            Object result = point.proceed();

            // 记录响应信息（只在开启INFO日志时执行）
            if (log.isInfoEnabled()) {
                long endTime = System.currentTimeMillis();
                logDataBuilder.duration(endTime - startTime)
                        .responseTime(endTime)
                        .responseStatus(HttpStatus.OK.value());
            }

            LogUtils.info("Request completed", logDataBuilder.build());

            return result;
        } catch (Exception e) {
            // 记录错误信息（错误日志通常都是开启的）
            long endTime = System.currentTimeMillis();
            logDataBuilder.duration(endTime - startTime)
                    .responseTime(endTime)
                    .responseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

            LogUtils.error("Request failed", e, logDataBuilder.build());
            throw e;
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip;
        for (String header : IP_HEADERS) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                int idx = ip.indexOf(COMMA);
                return idx > 0 ? ip.substring(0, idx).trim() : ip;
            }
        }
        return request.getRemoteAddr();
    }

    private boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip);
    }
}

