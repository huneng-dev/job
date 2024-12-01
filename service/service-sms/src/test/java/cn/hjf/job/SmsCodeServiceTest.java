package cn.hjf.job;

import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.rabbit.service.RabbitService;
import cn.hjf.job.sms.service.SmsCodeService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmsCodeServiceTest {

    @Resource
    private SmsCodeService smsCodeService;

    @Resource
    private RabbitService rabbitService;

    @Test
    public void sendSmsCode() {
        boolean b = smsCodeService.sendSmsCode("17629985126", "123321", "5");
        System.out.println("发送状态：" + b);
    }

    @Test
    public void phoneRegisterVerificationCodeTest() {
        rabbitService.sendMessage(
                MqConst.EXCHANGE_PHONE,
                MqConst.ROUTING_PHONE_REGISTER,
                "17629985126"
        );
    }
}
