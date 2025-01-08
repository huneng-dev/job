package cn.hjf.job.model.vo.resume;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "工作经历")
public class WorkExperienceVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "简历id")
    private Long resumeId;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "行业id")
    private Long industryId;

    @Schema(description = "开始时间")
    private LocalDate startDate;

    @Schema(description = "结束时间")
    private LocalDate endDate;

    @Schema(description = "职位id")
    private Long positionId;

    @Schema(description = "工作描述")
    private String jobDescription;

    @Schema(description = "是否是实习 0表示不是，1表示是")
    private Integer isInternship;

    @Schema(description = "是否对这家公司隐藏 0表示不隐藏，1表示隐藏")
    private Integer isHidden;
}
