package cn.hjf.job.user.service;

import cn.hjf.job.model.form.user.PhonePasswordVerifyForm;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserInfoServiceTest {

    @Resource
    private UserInfoService userInfoService;

    @Test
    public void phonePasswordVerify() {
        System.err.println(
                userInfoService
                        .phonePasswordVerify(
                                new PhonePasswordVerifyForm("", "")
                        )
        );
        System.err.println(
                userInfoService
                        .phonePasswordVerify(
                                new PhonePasswordVerifyForm("17629985126", "123456")
                        )
        );
        System.err.println(
                userInfoService
                        .phonePasswordVerify(
                                new PhonePasswordVerifyForm("17629981111", "123456")
                        )
        );
        System.err.println(
                userInfoService
                        .phonePasswordVerify(
                                new PhonePasswordVerifyForm("17629981111", "111")
                        )
        );

    }
}
