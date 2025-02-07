package cn.hjf.job.common.logging.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LogData {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    private String level;
    private String service;
    private String thread;
    private Long threadId;
    private String traceId;
    private String spanId;
    private String parentSpanId;
    private String user;
    private String className;
    private String methodName;
    private String message;
    private Object[] args;
    private String exception;
    private String exceptionMessage;
}
