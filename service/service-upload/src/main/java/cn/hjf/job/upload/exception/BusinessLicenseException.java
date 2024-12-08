package cn.hjf.job.upload.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessLicenseException extends RuntimeException{
    public BusinessLicenseException(String message) {
        super(message);
    }

    public BusinessLicenseException(String message, Throwable cause) {
        super(message, cause);
    }
}
