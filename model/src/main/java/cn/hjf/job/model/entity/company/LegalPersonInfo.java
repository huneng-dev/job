package cn.hjf.job.model.entity.company;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
/**
 * 
 * @author hjf
 * @date 2024-10-31
 */
@Data
@Schema(description = "LegalPersonInfo")
@TableName("legal_person_info")
public class LegalPersonInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "姓名")
    @TableField("name")
    private String name;

    @Schema(description = "身份证号")
    @TableField("idcard_no")
    private String idcardNo;

    @Schema(description = "出生日期")
    @TableField("birthday")
    private LocalDate birthday;

    @Schema(description = "地址")
    @TableField("idcard_address")
    private String idcardAddress;

    @Schema(description = "身份证有效日期")
    @TableField("idcard_expire")
    private LocalDate idcardExpire;

    @Schema(description = "性别 1:男 2:女")
    @TableField("gender")
    private Integer gender;

    @Schema(description = "身份证正面url")
    @TableField("idcard_front_url")
    private String idcardFrontUrl;

    @Schema(description = "身份证反面url")
    @TableField("idcard_back_url")
    private String idcardBackUrl;

    @Schema(description = "腾讯云人脸识别模型id")
    @TableField("face_model_id")
    private String faceModelId;

    @Schema(description = "名下企业数量")
    @TableField("company_count")
    private Integer companyCount;

    @Schema(description = "1表示正常，2表示禁用")
    @TableField("status")
    private Integer status;
    }
