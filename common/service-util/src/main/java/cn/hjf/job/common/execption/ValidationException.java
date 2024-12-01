package cn.hjf.job.common.execption;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}