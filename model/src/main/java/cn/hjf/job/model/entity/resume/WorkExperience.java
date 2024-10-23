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
@Schema(description = "WorkExperience")
@TableName("work_experience")
public class WorkExperience extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "简历id")
    @TableField("work_experience")
    private Long resumeId;

    @Schema(description = "公司名称")
    @TableField("work_experience")
    private String companyName;

    @Schema(description = "行业id")
    @TableField("work_experience")
    private Long industryId;

    @Schema(description = "开始时间")
    @TableField("work_experience")
    private LocalDate startDate;

    @Schema(description = "结束时间 （null 表示为至今）")
    @TableField("work_experience")
    private LocalDate endDate;

    @Schema(description = "职位id")
    @TableField("work_experience")
    private Long positionId;

    @Schema(description = "工作描述")
    @TableField("work_experience")
    private String jobDescription;

    @Schema(description = "是否是实习 0表示不是，1表示是")
    @TableField("work_experience")
    private Integer isInternship;

    @Schema(description = "是否对这家公司隐藏 0表示不隐藏，1表示隐藏")
    @TableField("work_experience")
    private Integer isHidden;
    }
