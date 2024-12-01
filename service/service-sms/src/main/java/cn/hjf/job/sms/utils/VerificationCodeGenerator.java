package cn.hjf.job.sms.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class VerificationCodeGenerator {

    public String generateVerificationCode() {
        // 生成一个 6 位数字验证码
        int verificationCode = 100000 + ThreadLocalRandom.current().nextInt(900000); // 100000 到 999999
        return String.valueOf(verificationCode);  // 转为字符串
    }
}
