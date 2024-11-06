package cn.hjf.job.model.enums.auth;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum LoginMethodType {
    PHONE(1, "手机号登录"),
    EMAIL(2,"邮箱登录"),
    WEIXIN(3,"微信登录");


    private Integer type;
    private String comment;

    LoginMethodType(Integer type, String comment) {
        this.type = type;
        this.comment = comment;
    }
}
