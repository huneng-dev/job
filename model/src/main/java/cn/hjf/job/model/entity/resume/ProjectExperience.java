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
 * @date 2024-10-31
 */
@Data
@Schema(description = "ProjectExperience")
@TableName("project_experience")
public class ProjectExperience extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "简历id")
    @TableField("resume_id")
    private Long resumeId;

    @Schema(description = "项目名称")
    @TableField("project_name")
    private String projectName;

    @Schema(description = "担任角色")
    @TableField("role")
    private String role;

    @Schema(description = "开始时间")
    @TableField("start_time")
    private LocalDate startTime;

    @Schema(description = "结束时间 （null 表示至今）")
    @TableField("end_time")
    private LocalDate endTime;

    @Schema(description = "项目描述 (MongoDB)")
    @TableField("project_description")
    private String projectDescription;

    @Schema(description = "项目链接")
    @TableField("project_url")
    private String projectUrl;
    }
