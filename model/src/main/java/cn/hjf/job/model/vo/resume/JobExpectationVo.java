package cn.hjf.job.model.vo.resume;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "工作需求")
public class JobExpectationVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "简历id")
    private Long resumeId;

    @Schema(description = "期望职位id")
    private Long expectedPositionId;

    @Schema(description = "期望城市（市）")
    private String workCity;

    @Schema(description = "期望薪资最低值，单位：千元(K)")
    private Integer salaryMin;

    @Schema(description = "期望薪资最高值，单位：千元(K)")
    private Integer salaryMax;

    @Schema(description = "是否面议，0 不是，1 是")
    private Integer isNegotiable;

    @Schema(description = "期望行业id")
    private Long industryId;

    @Schema(description = "工作类型")
    private Integer jobType;
}
