package cn.hjf.job.model.entity.recruiter;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
/**
 * 
 * @author hjf
 * @date 2024-10-23
 */
@Data
@Schema(description = "RecruiterInfo")
@TableName("recruiter_info")
public class RecruiterInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "公司id")
    @TableField("recruiter_info")
    private Long companyId;

    @Schema(description = "用户昵称")
    @TableField("recruiter_info")
    private String nckname;

    @Schema(description = "用户手机号")
    @TableField("recruiter_info")
    private String phone;

    @Schema(description = "用户邮箱")
    @TableField("recruiter_info")
    private String email;

    @Schema(description = "用户密码（加密）")
    @TableField("recruiter_info")
    private String password;

    @Schema(description = "用户真实姓名")
    @TableField("recruiter_info")
    private String name;

    @Schema(description = "性别 1:男 2:女")
    @TableField("recruiter_info")
    private String gender;

    @Schema(description = "出生日期")
    @TableField("recruiter_info")
    private LocalDate birthday;

    @Schema(description = "身份证编号")
    @TableField("recruiter_info")
    private String idcardNo;

    @Schema(description = "身份证地址")
    @TableField("recruiter_info")
    private String idcardAddress;

    @Schema(description = "身份证有效日期")
    @TableField("recruiter_info")
    private LocalDate idcardExpire;

    @Schema(description = "身份证正面 url")
    @TableField("recruiter_info")
    private String idcardFrontUrl;

    @Schema(description = "身份证反面 url")
    @TableField("recruiter_info")
    private String idcardBackUrl;

    @Schema(description = "手持身份证 url")
    @TableField("recruiter_info")
    private String idcardHandUrl;

    @Schema(description = "腾讯云人脸模型id")
    @TableField("recruiter_info")
    private String faceModelId;

    @Schema(description = "人脸识别认证状态 0:未认证  1：审核中 2：认证通过 -1：认证未通过")
    @TableField("recruiter_info")
    private Integer authStatus;

    @Schema(description = "状态，1正常，2禁用")
    @TableField("recruiter_info")
    private Integer status;

    @Schema(description = "是否是超级管理员 0 表示不是 ，1表示是")
    @TableField("recruiter_info")
    private Integer isSuperAdmin;
    }
