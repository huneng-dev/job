package cn.hjf.job.sms.service;


/**
 * 短信验证码发送服务
 */
public interface SmsCodeService {

    /**
     * 发送注册、登录、找回密码的验证码
     *
     * @param phone 目标手机号
     * @param code  验证码
     * @param time  有效时间
     * @return 是否发送成功
     */
    boolean sendSmsCode(String phone, String code, String time);
}
