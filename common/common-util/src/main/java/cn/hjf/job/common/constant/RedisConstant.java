package cn.hjf.job.common.constant;

public class RedisConstant {

    //用户登录
    public static final String USER_TOKEN = "user:token:";
    public static final String USER_LOGIN_REFRESH_KEY_PREFIX = "user:login:refresh:";
    public static final int USER_TOKEN_TIME_OUT = 60 * 60 * 24 * 100; // 1 天

    public static final int PUBLIC_COMPANY_INDUSTRY_TIME_OUT = 60 * 60 * 24; // 1 天
    public static final String COMPANY_INDUSTRY_PARENT = "company:industry:parent"; // 存储全部父行业
    public static final String COMPANY_SUB_INDUSTRIES = "company:sub:industries:"; // 存储父行业以及其下的子行业
    public static final String INDUSTRY_POSITION_TYPE_S = "industry:position:"; // 存储全部子行业
}
