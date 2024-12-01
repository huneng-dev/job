package cn.hjf.job.email.service;

import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.rabbit.service.RabbitService;
import cn.hjf.job.model.vo.email.EmailVerificationCode;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class IEmailServiceTest {

    @Resource
    private IEmailService iEmailService;


    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private RabbitService rabbitService;

    @Test
    public void sendText() {
        iEmailService.sendText("huneng.code@foxmail.com", "huneng.code@gmail.com", "121JOB注册验证码", "123123");
    }

    @Test
    public void sendHtml() {
        Context context = new Context();
        context.setVariable("action", "注册");
        context.setVariable("verificationCode", "123321");
        String content = templateEngine.process("email.html", context);
        iEmailService.sendHtml("huneng.code@foxmail.com", "huneng.code@gmail.com", "121JOB注册验证码", content);
    }

    @Test
    public void registerVerificationCode() {
        EmailVerificationCode emailVerificationCode = new EmailVerificationCode();
        emailVerificationCode.setTo("1051174451@qq.com");
        emailVerificationCode.setAction("登录");
//         2.发送MQ消息
        rabbitService.sendMessage(MqConst.EXCHANGE_EMAIL,
                MqConst.ROUTING_EMAIL_REGISTER,
                emailVerificationCode
        );
    }
}
