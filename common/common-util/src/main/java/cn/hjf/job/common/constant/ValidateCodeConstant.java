package cn.hjf.job.common.constant;

import cn.hjf.job.model.vo.company.IndustryVo;

public class ValidateCodeConstant {

    // 邮件验证码有效期
    public static final Integer EMAIL_CODE_EXPIRATION_TIME = 60 * 5;

    // 邮件验证码获取间隔
    public static final Integer EMAIL_CODE_INTERVAL = 60;

    // 手机号验证码相关的常量
    public static final Integer PHONE_CODE_EXPIRATION_TIME = 60 * 5;  // 手机验证码有效期：5分钟

    public static final Integer PHONE_CODE_INTERVAL = 60;  // 手机验证码获取间隔：60秒

    public static final Integer COMPANY_ADD_CODE_TIME = 60 * 10; // 加入公司验证码

    public static final Integer COMPANY_ADD_CODE_INTERVAL = 60;

    public static final Integer COMPANY_ADD_CODE_EXPIRATION_TIME = 30 * 60;

}
