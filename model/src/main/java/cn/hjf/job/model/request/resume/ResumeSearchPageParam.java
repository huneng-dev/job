package cn.hjf.job.model.request.resume;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "简历搜索参数")
public class ResumeSearchPageParam {

    @Schema(description = "搜索关键词")
    private String search;

    @Schema(description = "求职状态")
    private Integer jobStatus;

    @Schema(description = "行业")
    private Long industryId;

    @Schema(description = "期望职位id")
    private Long expectedPositionId;

    @Schema(description = "城市")
    private String workCity;

    @Schema(description = "最低薪资")
    private Integer salaryMin;

    @Schema(description = "最高薪资")
    private Integer salaryMax;

    @Schema(description = "工作类型")
    private Integer jobType;

    @Schema(description = "教育水平")
    private Integer educationLevel;

    @Schema(description = "得分")
    private Double score;

    @Schema(description = "更新时间")
    private Long updateTime;

}
