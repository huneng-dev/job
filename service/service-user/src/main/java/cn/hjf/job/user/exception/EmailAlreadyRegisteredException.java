package cn.hjf.job.user.exception;

public class EmailAlreadyRegisteredException extends RuntimeException{
    // 构造方法1：默认构造函数
    public EmailAlreadyRegisteredException() {
        super("The email address is already registered.");
    }

    // 构造方法2：接受自定义异常信息
    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }

    // 构造方法3：接受自定义异常信息和原因（嵌套的异常）
    public EmailAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    // 构造方法4：接受原因（嵌套的异常）
    public EmailAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }
}
