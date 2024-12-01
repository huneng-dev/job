package cn.hjf.job.common.util;

import cn.hjf.job.common.execption.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidationUtil {

    private final Validator validator;

    @Autowired
    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }

    /**
     * 校验对象
     *
     * @param object 校验对象
     * @param <T>    泛型类型
     * @throws ValidationException 校验失败时抛出异常
     */
    public <T> void validate(T object) throws ValidationException {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
            throw new ValidationException("校验失败: " + errorMessage);
        }
    }

    /**
     * 校验对象（带自定义错误消息）
     *
     * @param object       校验对象
     * @param errorMessage 自定义错误消息
     * @param <T>          泛型类型
     * @throws ValidationException 校验失败时抛出异常
     */
    public <T> void validateWithMessage(T object, String errorMessage) throws ValidationException {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            String errorDetails = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
            throw new ValidationException(errorMessage + ": " + errorDetails);
        }
    }
}
