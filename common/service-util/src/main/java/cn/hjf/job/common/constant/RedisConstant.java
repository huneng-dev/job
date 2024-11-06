package cn.hjf.job.common.constant;

public class RedisConstant {

    //用户登录
    public static final String USER_TOKEN = "user:token:";
    public static final String USER_LOGIN_REFRESH_KEY_PREFIX = "user:login:refresh:";
    public static final int USER_TOKEN_TIME_OUT = 60 * 60 * 24 * 100; // 1 天
}
