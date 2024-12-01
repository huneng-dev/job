package cn.hjf.job.sms.receiver;

import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.constant.ValidateCodeConstant;
import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.common.result.ResultCodeEnum;
import cn.hjf.job.sms.service.SmsCodeService;
import cn.hjf.job.sms.utils.VerificationCodeGenerator;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class VerificationCodeReceiver {

    @Resource
    private SmsCodeService smsCodeService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private VerificationCodeGenerator verificationCodeGenerator;

    /**
     * 生成并发送短信验证码
     *
     * @param phone          目标手机号
     * @param redisKeyPrefix 验证码存储的 Redis Key 前缀
     */
    private void generateAndSendSmsCode(String phone, String redisKeyPrefix) {
        // 二次确认发送时间间隔 > 1分钟
        // 判断 Redis 中是否有该 key，并获取剩余时间
        Long expire = redisTemplate.getExpire(redisKeyPrefix + phone);

        // 判断是否存在并且是否超过一定的过期时间
        if (expire != null && expire >= (ValidateCodeConstant.PHONE_CODE_EXPIRATION_TIME - ValidateCodeConstant.PHONE_CODE_INTERVAL)) {
            return;
        }

        // 生成验证码
        String verificationCode = verificationCodeGenerator.generateVerificationCode();

        // 保存到redis中，设置过期时间
        redisTemplate.opsForValue().set(
                redisKeyPrefix + phone,
                verificationCode,
                (long) ValidateCodeConstant.PHONE_CODE_EXPIRATION_TIME,
                TimeUnit.SECONDS
        );

        // 发送短信
        smsCodeService.sendSmsCode(phone,
                verificationCode,
                String.valueOf(((long) ValidateCodeConstant.PHONE_CODE_EXPIRATION_TIME / 60)) // 发送短信时有效期以分钟为单位
        );
    }

    /**
     * 发送手机注册验证码
     *
     * @param phone   目标手机号
     * @param message MQ消息
     * @param channel 消息处理
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_PHONE_REGISTER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_PHONE),
            key = {MqConst.ROUTING_PHONE_REGISTER}
    ))
    public void phoneRegisterVerificationCode(String phone, Message message, Channel channel) {
        generateAndSendSmsCode(phone, RedisConstant.PHONE_REGISTER_CODE);
    }

    /**
     * 发送手机找回密码验证码
     *
     * @param phone   目标手机号
     * @param message MQ消息
     * @param channel 消息处理
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_PHONE_RECOVERY, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_PHONE),
            key = {MqConst.ROUTING_PHONE_RECOVERY}
    ))
    public void phoneRecoveryVerificationCode(String phone, Message message, Channel channel) {
        generateAndSendSmsCode(phone, RedisConstant.PHONE_RECOVERY_CODE);
    }

    /**
     * 发送手机登录验证码
     *
     * @param phone   目标手机号
     * @param message MQ消息
     * @param channel 消息处理
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_PHONE_LOGIN, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_PHONE),
            key = {MqConst.ROUTING_PHONE_LOGIN}
    ))
    public void phoneLoginVerificationCode(String phone, Message message, Channel channel) {
        generateAndSendSmsCode(phone, RedisConstant.PHONE_LOGIN_CODE);
    }
}
