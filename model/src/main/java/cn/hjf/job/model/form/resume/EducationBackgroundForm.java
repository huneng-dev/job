package cn.hjf.job.model.form.resume;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "教育背景表单")
public class EducationBackgroundForm {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "简历id")
    private Long resumeId;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "专业名称")
    private String major;

    @Schema(description = "教育水平")
    private Integer educationLevel;

    @Schema(description = "是否全日制")
    private Integer isFullTime;

    @Schema(description = "开始年")
    private Integer startYear;

    @Schema(description = "结束年")
    private Integer endYear;
}
