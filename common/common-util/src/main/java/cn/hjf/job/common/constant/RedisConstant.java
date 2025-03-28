package cn.hjf.job.common.constant;

import java.util.Map;

public class RedisConstant {

    //用户登录
    public static final String USER_TOKEN = "user:token:";
    public static final String USER_LOGIN_REFRESH_KEY_PREFIX = "user:login:refresh:";
    public static final int USER_TOKEN_TIME_OUT = 60 * 60 * 24 * 100; // 1 天

    public static final int PUBLIC_COMPANY_INDUSTRY_TIME_OUT = 60 * 60 * 24; // 1 天
    public static final String COMPANY_INDUSTRY_PARENT = "company:industry:parent"; // 存储全部父行业
    public static final String COMPANY_SUB_INDUSTRIES = "company:sub:industries:"; // 存储父行业以及其下的子行业
    public static final String INDUSTRY_POSITION_TYPE_S = "industry:position:"; // 存储全部子行业

    // 邮箱验证码相关的常量
    public static final String EMAIL_REGISTER_CODE = "email:code:register:"; // 注册验证码 Key
    public static final String EMAIL_RECOVERY_CODE = "email:code:recovery:"; // 找回密码验证码 Key
    public static final String EMAIL_LOGIN_CODE = "email:code:login:"; // 登录验证码 Key

    // 手机号验证码相关的常量
    public static final String PHONE_REGISTER_CODE = "phone:code:register:"; // 注册验证码 Key
    public static final String PHONE_RECOVERY_CODE = "phone:code:recovery:"; // 找回密码验证码 Key
    public static final String PHONE_LOGIN_CODE = "phone:code:login:"; // 登录验证码 Key

    // 加入公司验证码相关常量
    public static final String COMPANY_ADD_CODE = "company:add:code:";

    public static final String COMPANY_ADD_USER = "company:add:user:";


    //等待获取锁的时间
    public static final Integer USER_INFO_OPERATE_LOCK_WAIT_TIME = 1;

    //加锁的时间
    public static final Integer USER_INFO_OPERATE_LOCK_LEASE_TIME = 1;


    // 注册分布式锁的邮箱前缀
    public static final String EMAIL_REGISTER_LOCK_PREFIX = "register:email:";

    // 注册分布式锁的手机号前缀
    public static final String PHONE_REGISTER_LOCK_PREFIX = "register:phone:";

    // 密码登录尝试次数和最后时间相关前缀
    public static final String PASSWORD_LOGIN_ATTEMPTS_PREFIX = "login:attempts:";

    // 账户冷却时间，单位为毫秒（30 分钟）
    public static final int LOCKED_ACCOUNT_COOL_DOWN_TIME_MS = 30 * 60 * 1000;

    public static final Map<Integer, String> USER_TYPE_MAP = Map.of(
            1, "candidate:",
            2, "recruiter:"
    );

    // 应聘端职位信息
    public static final String POSITION_INFO_CANDIDATE = "position:candidate:";

    // 应聘端职位信息缓存时间 (一天 单位秒)
    public static final int POSITION_INFO_CANDIDATE_TIME_OUT = 60 * 60 * 24;

    // 公司信息缓存
    public static final String COMPANY_INFO = "company:info:";

    // 公司信息缓存时间 (一天 单位秒)
    public static final int COMPANY_INFO_TIME_OUT = 60 * 60 * 24;

    // 公司地址信息缓存
    public static final String COMPANY_ADDRESS = "company:address:";

    // 公司地址缓存时间 (一天 单位秒)
    public static final int COMPANY_ADDRESS_TIME_OUT = 60 * 60 * 24;

    // 聊天关系缓存
    public static final String CHAT_RELATION_CACHE = "chat:relation:";

    // 聊天关系缓存时间
    public static final int CHAT_RELATION_CACHE_TIME = 60 * 60 * 24;

    // 聊天关系查询分布式锁
    public static final String CHAT_RELATION_LOCK = "chat:relation:lock:";

    // 聊天关系查询分布式锁等待时间
    public static final int CHAT_RELATION_LOCK_WAIT_TIME = 5;

    // 聊天关系查询分布式锁加锁时间
    public static final int CHAT_RELATION_LOCK_LEASE_TIME = 5;

    public static final String UPLOAD_STATUS = "upload:status:";
}
