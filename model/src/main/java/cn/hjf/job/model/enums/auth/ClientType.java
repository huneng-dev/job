package cn.hjf.job.model.enums.auth;

import lombok.Getter;

@Getter
public enum ClientType {

    WEB_CANDIDATE(1, "应聘者WEB端");

    private Integer type;

    private String comment;

    ClientType(Integer type, String comment) {
        this.type = type;
        this.comment = comment;
    }
}
