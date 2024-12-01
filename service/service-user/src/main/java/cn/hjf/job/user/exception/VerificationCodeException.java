package cn.hjf.job.user.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerificationCodeException extends RuntimeException {

    // 设置错误码
    // 获取错误码
    // 可选：用于存放额外的错误信息或错误码
    private String errorCode;

    // 默认构造函数
    public VerificationCodeException() {
        super();
    }

    // 带错误消息的构造函数
    public VerificationCodeException(String message) {
        super(message);
    }

    // 带错误消息和错误码的构造函数
    public VerificationCodeException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
