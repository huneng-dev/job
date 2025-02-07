package cn.hjf.job.email.receiver;

import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.constant.ValidateCodeConstant;
import cn.hjf.job.email.service.IEmailService;
import cn.hjf.job.email.utils.VerificationCodeGenerator;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.Setter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import cn.hjf.job.common.rabbit.constant.MqConst;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.concurrent.TimeUnit;

@Component
public class VerificationCodeReceiver {

    @Value("${spring.mail.username}")  // 使用配置文件中的值
    @Setter
    private String form;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private IEmailService iEmailService;

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private VerificationCodeGenerator verificationCodeGenerator;


    /**
     * 邮件注册验证码
     *
     * @param to      目标邮箱
     * @param message MQ消息
     * @param channel 消息处理
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_EMAIL_REGISTER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_EMAIL),
            key = {MqConst.ROUTING_EMAIL_REGISTER}
    ))
    public void emailRegisterVerificationCode(String to, Message message, Channel channel) {
        sendVerificationCode(to, "注册", RedisConstant.EMAIL_REGISTER_CODE);
    }


    /**
     * 邮件找回密码验证码
     *
     * @param to      目标邮箱
     * @param message MQ消息
     * @param channel 消息处理
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_EMAIL_RECOVERY, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_EMAIL),
            key = {MqConst.ROUTING_EMAIL_RECOVERY}
    ))
    public void emailRecoveryVerificationCode(String to, Message message, Channel channel) {
        sendVerificationCode(to, "找回密码", RedisConstant.EMAIL_RECOVERY_CODE);
    }

    /**
     * 邮件登录验证码
     *
     * @param to      目标邮箱
     * @param message MQ消息
     * @param channel 消息处理
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_EMAIL_LOGIN, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_EMAIL),
            key = {MqConst.ROUTING_EMAIL_LOGIN}
    ))
    public void emailLoginVerificationCode(String to, Message message, Channel channel) {
        sendVerificationCode(to, "登录", RedisConstant.EMAIL_LOGIN_CODE);
    }


    public void sendVerificationCode(String to, String action, String redisKeyPrefix) {
        // 1.生成验证码
        String verificationCode = verificationCodeGenerator.generateVerificationCode();

        // 2.保存到redis中，设置过期时间
        storeVerificationCode(redisKeyPrefix + to, verificationCode);

        // 3.设置模板参数
        Context context = new Context();
        context.setVariable("action", action);
        context.setVariable("verificationCode", verificationCode);

        // 4.使用模板生成邮件内容
        String content = templateEngine.process("email.html", context);

        // 5.发送HTML邮件
        iEmailService.sendHtml(form, to, "121JOB-" + action + "验证码", content);
    }

    @Async
    public void storeVerificationCode(String key, String verificationCode) {
        redisTemplate.opsForValue().set(key,
                verificationCode,
                ValidateCodeConstant.EMAIL_CODE_EXPIRATION_TIME,
                TimeUnit.SECONDS
        );
    }
}
