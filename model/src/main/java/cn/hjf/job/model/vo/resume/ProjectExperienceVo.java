package cn.hjf.job.model.vo.resume;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "项目经验")
public class ProjectExperienceVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "简历id")
    private Long resumeId;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "担任角色")
    private String role;

    @Schema(description = "开始时间")
    private LocalDate startTime;

    @Schema(description = "结束时间 （null 表示至今）")
    private LocalDate endTime;

    @Schema(description = "项目描述")
    private String projectDescription;

    @Schema(description = "项目链接")
    private String projectUrl;
}
