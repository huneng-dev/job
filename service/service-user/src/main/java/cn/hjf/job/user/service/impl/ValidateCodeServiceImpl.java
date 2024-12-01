package cn.hjf.job.user.service.impl;

import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.rabbit.service.RabbitService;
import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.constant.ValidateCodeConstant;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.common.result.ResultCodeEnum;
import cn.hjf.job.user.service.ValidateCodeService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private RabbitService rabbitService;

    @Override
    public Result<String> sendEmailRegisterValidateCode(String to) {
        return sendEmailValidateCode(to, RedisConstant.EMAIL_REGISTER_CODE, MqConst.ROUTING_EMAIL_REGISTER, "注册");
    }

    @Override
    public Result<String> sendEmailRecoveryValidateCode(String to) {
        return sendEmailValidateCode(to, RedisConstant.EMAIL_RECOVERY_CODE, MqConst.ROUTING_EMAIL_RECOVERY, "找回密码");
    }

    @Override
    public Result<String> sendEmailLoginValidateCode(String to) {
        return sendEmailValidateCode(to, RedisConstant.EMAIL_LOGIN_CODE, MqConst.ROUTING_EMAIL_LOGIN, "登录");
    }

    @Override
    public Result<String> sendPhoneRegisterValidateCode(String phone) {
        return sendPhoneValidateCode(phone, RedisConstant.PHONE_REGISTER_CODE, MqConst.ROUTING_PHONE_REGISTER, "注册");
    }

    @Override
    public Result<String> sendPhoneRecoveryValidateCode(String phone) {
        return sendPhoneValidateCode(phone, RedisConstant.PHONE_RECOVERY_CODE, MqConst.ROUTING_PHONE_RECOVERY, "找回密码");
    }

    @Override
    public Result<String> sendPhoneLoginValidateCode(String phone) {
        return sendPhoneValidateCode(phone, RedisConstant.PHONE_LOGIN_CODE, MqConst.ROUTING_PHONE_LOGIN, "登录");
    }

    /**
     * 发送手机验证码（注册、找回密码、登录）
     *
     * @param phone          目标手机号
     * @param redisKeyPrefix 验证码存储的 Redis Key 前缀
     * @param routingKey     消息队列的路由键
     * @param action         验证码操作类型（例如 "注册"、"找回密码"、"登录"）
     * @return Result<String> 结果
     */
    private Result<String> sendPhoneValidateCode(String phone, String redisKeyPrefix, String routingKey, String action) {
        // 判断 Redis 中是否有该 key，并获取剩余时间
        Long expire = redisTemplate.getExpire(redisKeyPrefix + phone);

        // 判断是否存在并且是否超过一定的过期时间
        if (expire != null && expire >= (ValidateCodeConstant.PHONE_CODE_EXPIRATION_TIME - ValidateCodeConstant.PHONE_CODE_INTERVAL)) {
            return Result.build(String.valueOf(expire - (ValidateCodeConstant.PHONE_CODE_EXPIRATION_TIME - ValidateCodeConstant.PHONE_CODE_INTERVAL)), ResultCodeEnum.VERIFY_CODE_TOO_FREQUENT);
        }

        // 发送验证码
        boolean success = rabbitService.sendMessage(MqConst.EXCHANGE_PHONE, routingKey, phone);

        if (!success) {
            return Result.build("验证码获取失败", ResultCodeEnum.VERIFY_CODE_REQUEST_FAILED);
        }

        return Result.ok("发送" + action + "验证码成功");
    }


    /**
     * 发送邮箱验证码
     *
     * @param to             邮箱地址
     * @param redisKeyPrefix Redis key 前缀
     * @param routingKey     RabbitMQ 路由键
     * @param action         验证码的操作名称（如：注册、找回密码、登录）
     * @return 发送状态
     */
    private Result<String> sendEmailValidateCode(String to, String redisKeyPrefix, String routingKey, String action) {
        // 判断 Redis 中是否有该 key，并获取剩余时间
        Long expire = redisTemplate.getExpire(redisKeyPrefix + to);

        // 判断是否存在并且是否超过一定的过期时间
        if (expire != null && expire >= (ValidateCodeConstant.EMAIL_CODE_EXPIRATION_TIME - ValidateCodeConstant.EMAIL_CODE_INTERVAL)) {
            return Result.build(String.valueOf(expire - (ValidateCodeConstant.EMAIL_CODE_EXPIRATION_TIME - ValidateCodeConstant.EMAIL_CODE_INTERVAL)), ResultCodeEnum.VERIFY_CODE_TOO_FREQUENT);
        }

        // 发送验证码
        boolean success = rabbitService.sendMessage(MqConst.EXCHANGE_EMAIL, routingKey, to);

        if (!success) {
            return Result.build("验证码获取失败", ResultCodeEnum.VERIFY_CODE_REQUEST_FAILED);
        }
        return Result.ok("发送" + action + "验证码成功");
    }


}
