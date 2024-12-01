package cn.hjf.job.user.exception;

public class PhoneAlreadyRegisterException extends RuntimeException {

    public PhoneAlreadyRegisterException() {
        super("手机号已注册！");
    }

    public PhoneAlreadyRegisterException(String message) {
        super(message);
    }
}
