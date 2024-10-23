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
 * @date 2024-10-23
 */
@Data
@Schema(description = "LegalPersonInfo")
@TableName("legal_person_info")
public class LegalPersonInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "姓名")
    @TableField("legal_person_info")
    private String name;

    @Schema(description = "身份证号")
    @TableField("legal_person_info")
    private String idcardNo;

    @Schema(description = "出生日期")
    @TableField("legal_person_info")
    private LocalDate birthday;

    @Schema(description = "地址")
    @TableField("legal_person_info")
    private String idcardAddress;

    @Schema(description = "身份证有效日期")
    @TableField("legal_person_info")
    private LocalDate idcardExpire;

    @Schema(description = "性别 1:男 2:女")
    @TableField("legal_person_info")
    private Integer gender;

    @Schema(description = "身份证正面url")
    @TableField("legal_person_info")
    private String idcardFrontUrl;

    @Schema(description = "身份证反面url")
    @TableField("legal_person_info")
    private String idcardBackUrl;

    @Schema(description = "腾讯云人脸识别模型id")
    @TableField("legal_person_info")
    private String faceModelId;

    @Schema(description = "名下企业数量")
    @TableField("legal_person_info")
    private Integer companyCount;

    @Schema(description = "1表示正常，2表示禁用")
    @TableField("legal_person_info")
    private Integer status;
    }
