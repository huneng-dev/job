package cn.hjf.job.model.entity.resume;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author hjf
 * @date 2024-10-31
 */
@Data
@Schema(description = "WorkExperience")
@TableName("work_experience")
public class WorkExperience extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "简历id")
    @TableField("resume_id")
    private Long resumeId;

    @Schema(description = "公司名称")
    @TableField("company_name")
    private String companyName;

    @Schema(description = "行业id")
    @TableField("industry_id")
    private Long industryId;

    @Schema(description = "开始时间")
    @TableField("start_date")
    private LocalDate startDate;

    @Schema(description = "结束时间 （null 表示为至今）")
    @TableField("end_date")
    private LocalDate endDate;

    @Schema(description = "职位")
    @TableField("position")
    private String position;

    @Schema(description = "工作描述")
    @TableField("job_description")
    private String jobDescription;

    @Schema(description = "是否是实习 0表示不是，1表示是")
    @TableField("is_internship")
    private Integer isInternship;
}
