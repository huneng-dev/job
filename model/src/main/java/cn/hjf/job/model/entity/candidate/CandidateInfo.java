package cn.hjf.job.model.entity.candidate;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 
 * @author hjf
 * @date 2024-10-24
 */
@Data
@Schema(description = "CandidateInfo")
@TableName("candidate_info")
public class CandidateInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "用户昵称")
    @TableField("nickname")
    private String nickname;

    @Schema(description = "手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "邮箱地址")
    @TableField("email")
    private String email;

    @Schema(description = "登录密码(加密存储)")
    @TableField("password")
    private String password;

    @Schema(description = "真实姓名")
    @TableField("name")
    private String name;

    @Schema(description = "性别  0:不便透露 1:男 2:女")
    @TableField("gender")
    private Integer gender;

    @Schema(description = "出生日期")
    @TableField("birthday")
    private LocalDate birthday;

    @Schema(description = "身份证号")
    @TableField("idcard_no")
    private String idcardNo;

    @Schema(description = "身份证地址")
    @TableField("idcard_address")
    private String idcardAddress;

    @Schema(description = "身份证有效日期")
    @TableField("idcard_expire")
    private LocalDate idcardExpire;

    @Schema(description = "身份证正面 url")
    @TableField("idcard_front_url")
    private String idcardFrontUrl;

    @Schema(description = "身份证背面 url")
    @TableField("idcard_back_url")
    private String idcardBackUrl;

    @Schema(description = "手持身份证 url")
    @TableField("idcard_hand_url")
    private String idcardHandUrl;

    @Schema(description = "腾讯云人脸模型id")
    @TableField("face_model_id")
    private String faceModelId;

    @Schema(description = "人脸识别认证状态 0:未认证  1：审核中 2：认证通过 -1：认证未通过")
    @TableField("auth_status")
    private Integer authStatus;

    @Schema(description = "状态，1正常，2禁用")
    @TableField("status")
    private Integer status;
    }
