package cn.hjf.job.common.logging.core;

import cn.hjf.job.common.logging.config.ApplicationConfig;
import cn.hjf.job.common.logging.config.JsonConfig;
import cn.hjf.job.common.logging.model.LogData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LogUtils {
    private static final ObjectMapper objectMapper = JsonConfig.createObjectMapper();
    private static final String SERVICE_NAME = ApplicationConfig.getServiceName();
    private static final String UNKNOWN = "unknown";

    public static void log(LogLevel level, String message, Object... args) {
        if (!isLevelEnabled(level)) {
            return;
        }

        try {
            LogData logData = buildLogData(level, message, args);
            String jsonLog = objectMapper.writeValueAsString(logData);

            switch (level) {
                case ERROR -> log.error(jsonLog);
                case WARN -> log.warn(jsonLog);
                case INFO -> log.info(jsonLog);
                case DEBUG -> log.debug(jsonLog);
                case TRACE -> log.trace(jsonLog);
            }
        } catch (Exception e) {
            log.error("Failed to create log entry", e);
        }
    }

    private static boolean isLevelEnabled(LogLevel level) {
        return switch (level) {
            case ERROR -> log.isErrorEnabled();
            case WARN -> log.isWarnEnabled();
            case INFO -> log.isInfoEnabled();
            case DEBUG -> log.isDebugEnabled();
            case TRACE -> log.isTraceEnabled();
        };
    }

    private static LogData buildLogData(LogLevel level, String message, Object... args) {
        try {
            Thread currentThread = Thread.currentThread();

            LogData.LogDataBuilder builder = LogData.builder()
                    .timestamp(LocalDateTime.now())
                    .level(level.name())
                    .service(SERVICE_NAME)
                    .thread(currentThread.getName())
                    .threadId(currentThread.getId())
                    .traceId(MDC.get("traceId"))
                    .spanId(MDC.get("spanId"))
                    .parentSpanId(MDC.get("parentSpanId"))
                    .message(message);

            // 获取调用者信息，发生异常则静默处理
            try {
                StackWalker.getInstance()
                        .walk(frames -> frames
                                .dropWhile(frame -> frame.getClassName().equals(LogUtils.class.getName()))
                                .findFirst())
                        .ifPresent(frame -> {
                            builder.className(frame.getClassName())
                                    .methodName(frame.getMethodName());
                        });
            } catch (Exception ignored) {
                // 静默处理异常，不收集调用者信息
            }

            // 获取用户信息
            try {
                String user = SecurityContextHolder.getContext().getAuthentication().getName();
                builder.user(user != null ? user : UNKNOWN);
            } catch (Exception e) {
                builder.user(UNKNOWN);
            }

            // 处理参数
            if (args != null && args.length > 0) {
                if (args.length == 1 && args[0] instanceof Throwable throwable) {
                    builder.exception(throwable.getClass().getName())
                            .exceptionMessage(throwable.getMessage());
                } else {
                    builder.args(args);
                }
            }

            return builder.build();
        } catch (Exception e) {
            return LogData.builder()
                    .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                    .level(level.name())
                    .service(SERVICE_NAME)
                    .message(message)
                    .build();
        }
    }

    // 对外提供的日志方法
    public static void error(String message, Object... args) {
        log(LogLevel.ERROR, message, args);
    }

    public static void warn(String message, Object... args) {
        log(LogLevel.WARN, message, args);
    }

    public static void info(String message, Object... args) {
        log(LogLevel.INFO, message, args);
    }

    public static void debug(String message, Object... args) {
        log(LogLevel.DEBUG, message, args);
    }

    public static void trace(String message, Object... args) {
        log(LogLevel.TRACE, message, args);
    }
}
