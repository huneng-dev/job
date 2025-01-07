package cn.hjf.job.model.form.resume;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "简历基本信息表单")
public class ResumeInfoForm {

    @Schema(description = "简历 id")
    private Long id;

    @Schema(description = "简历别称")
    private String resumeName;

    @Schema(description = "求职状态")
    private Integer jobStatus;

    @Schema(description = "个人优势")
    private String personalAdvantages;

    @Schema(description = "专业技能")
    private String professionalSkills;

    @Schema(description = "默认展示")
    private Integer isDefaultDisplay;
}
