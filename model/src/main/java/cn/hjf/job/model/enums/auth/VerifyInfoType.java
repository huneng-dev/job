package cn.hjf.job.model.enums.auth;

import lombok.Getter;

@Getter
public enum VerifyInfoType {

    USER_NOT_EXIST(0, "用户不存在");

    private Integer type;
    private String comment;

    VerifyInfoType(Integer type, String comment) {
        this.type = type;
        this.comment = comment;
    }
}
