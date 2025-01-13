package cn.hjf.job.common.rabbit.constant;

public class MqConst {


    public static final String EXCHANGE_COMPANY = "job.company";
    // 行业缓存刷新
    public static final String ROUTING_REFRESH_INDUSTRY = "job.company.industry.refresh";
    public static final String QUEUE_REFRESH_INDUSTRY = "job.company.industry.refresh";

    // 校验公司信息和营业执照信息
    public static final String ROUTING_VALIDATE_COMPANY_BUSINESS_LICENSE = "job.company.company.business.license";
    public static final String QUEUE_VALIDATE_COMPANY_BUSINESS_LICENSE = "job.company.company.business.license";

    // 邮箱相关的交换机、路由、队列
    public static final String EXCHANGE_EMAIL = "job.email";
    // 发送注册验证码
    public static final String ROUTING_EMAIL_REGISTER = "job.email.register";
    public static final String QUEUE_EMAIL_REGISTER = "job.email.register";
    // 找回密码的路由和队列
    public static final String ROUTING_EMAIL_RECOVERY = "job.email.recovery"; // 找回密码路由
    public static final String QUEUE_EMAIL_RECOVERY = "job.email.recovery"; // 找回密码队列
    // 登录验证码的路由和队列
    public static final String ROUTING_EMAIL_LOGIN = "job.email.login"; // 登录路由
    public static final String QUEUE_EMAIL_LOGIN = "job.email.login"; // 登录队列


    // 手机号相关的交换机、路由、队列
    public static final String EXCHANGE_PHONE = "job.phone";

    // 注册验证码的路由和队列
    public static final String ROUTING_PHONE_REGISTER = "job.phone.register"; // 注册路由
    public static final String QUEUE_PHONE_REGISTER = "job.phone.register"; // 注册队列

    // 找回密码验证码的路由和队列
    public static final String ROUTING_PHONE_RECOVERY = "job.phone.recovery"; // 找回密码路由
    public static final String QUEUE_PHONE_RECOVERY = "job.phone.recovery"; // 找回密码队列

    // 登录验证码的路由和队列
    public static final String ROUTING_PHONE_LOGIN = "job.phone.login"; // 登录路由
    public static final String QUEUE_PHONE_LOGIN = "job.phone.login"; // 登录队列

    // ES 搜索 相关 交换机、路由、队列
    public static final String EXCHANGE_ES = "job.es";

    // 职位变化
    public static final String ROUTING_ES_POSITION = "job.es.position";
    public static final String QUEUE_ES_POSITION = "job.es.position";

    // 简历更新到 ES
    public static final String ROUTING_ES_RESUME = "job.es.resume";
    public static final String QUEUE_ES_RESUME = "job.es.resume";

    public static final String EXCHANGE_RESUME = "job.resume";
    public static final String ROUTING_RESUME_CACHE = "job.resume.cache";
    public static final String QUEUE_RESUME_CACHE = "job.resume.cache";

}
