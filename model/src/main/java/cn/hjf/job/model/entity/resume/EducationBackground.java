package cn.hjf.job.model.entity.resume;

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
@Schema(description = "EducationBackground")
@TableName("education_background")
public class EducationBackground extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "简历id")
    @TableField("education_background")
    private Long resumeId;

    @Schema(description = "学校名称")
    @TableField("education_background")
    private String schoolName;

    @Schema(description = "专业名称")
    @TableField("education_background")
    private String major;

    @Schema(description = "学历 0：不限|无 1：初中及以下  2：中专 3：高中 4：大专 5：本科 6：硕士 7：博士")
    @TableField("education_background")
    private Integer educationLevel;

    @Schema(description = "是否全日制")
    @TableField("education_background")
    private Integer isFullTime;

    @Schema(description = "开始时间")
    @TableField("education_background")
    private LocalDate startTime;

    @Schema(description = "结束时间 （null 表示至今）")
    @TableField("education_background")
    private LocalDate endTime;
    }
