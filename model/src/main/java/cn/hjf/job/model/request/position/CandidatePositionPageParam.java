package cn.hjf.job.model.request.position;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "应聘端职位搜索参数")
public class CandidatePositionPageParam {

    @Schema(description = "搜索关键词")
    private String search;

    @Schema(description = "公司 id")
    private Long companyId;

    @Schema(description = "职位类型 id")
    private Long positionTypeId;

    @Schema(description = "求职类型")
    private Integer positionType;

    @Schema(description = "经验要求")
    private Integer experienceRequirement;

    @Schema(description = "学历要求")
    private Integer educationRequirement;

    @Schema(description = "地区")
    private String district;

    @Schema(description = "最低薪资")
    private Integer minSalary;

    @Schema(description = "最高薪资")
    private Integer maxSalary;

    @Schema(description = "半径")
    private Integer distance;

    @Schema(description = "维度")
    private Double lat;

    @Schema(description = "经度")
    private Double lon;

    @Schema(description = "公司规模 id")
    private Integer companySizeId;

    @Schema(description = "得分")
    private Double score;

    @Schema(description = "更新时间")
    private Long updateTime;
}
