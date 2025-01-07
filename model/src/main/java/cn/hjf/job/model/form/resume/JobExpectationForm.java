package cn.hjf.job.model.form.resume;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "工作期望")
public class JobExpectationForm {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "简历 id")
    private Long resumeId;

    @Schema(description = "期望职位 id")
    private Long expectedPositionId;

    @Schema(description = "期望行业 id")
    private Long industryId;

    @Schema(description = "工作城市")
    private String workCity;

    @Schema(description = "期望薪资最低值")
    private Integer salaryMin;

    @Schema(description = "期望薪资最高值")
    private Integer salaryMax;

    @Schema(description = "是否面议")
    private Integer isNegotiable;
}
