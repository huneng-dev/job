package cn.hjf.job.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "真实姓名")
    private String name;

    @Schema(description = "性别  0:不便透露 1:男 2:女")
    private Integer gender;

    @Schema(description = "出生日期")
    private LocalDate birthday;

    @Schema(description = "身份证号")
    private String idcardNo;

    @Schema(description = "身份证地址")
    private String idcardAddress;

    @Schema(description = "身份证有效日期")
    private LocalDate idcardExpire;

    @Schema(description = "身份证正面 url")
    private String idcardFrontUrl;

    @Schema(description = "身份证背面 url")
    private String idcardBackUrl;

    @Schema(description = "腾讯云人脸模型id")
    private String faceModelId;

    @Schema(description = "人脸识别认证状态 0:未认证  1：审核中 2：认证通过 -1：认证未通过")
    private String authStatus;

    @Schema(description = "状态，1正常，2禁用")
    private Integer status;

    @Schema(description = "用户类型  1：应聘者 ，2：招聘者")
    private Integer type;

}
