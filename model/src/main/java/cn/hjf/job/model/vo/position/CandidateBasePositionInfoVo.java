package cn.hjf.job.model.vo.position;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "应聘端基础职位信息")
public class CandidateBasePositionInfoVo {

    @Schema(description = "职位 id")
    private Long id;

    @Schema(description = "职位名称")
    private String positionName;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "职位类型 id")
    private Long positionTypeId;

    @Schema(description = "求职类型")
    private Integer positionType;

    @Schema(description = "经验要求")
    private Integer experienceRequirement;

    @Schema(description = "学历要求")
    private Integer educationRequirement;

    @Schema(description = "最低薪资")
    private Integer minSalary;

    @Schema(description = "最高薪资")
    private Integer maxSalary;

    @Schema(description = "地区 | 县")
    private String district;

    @Schema(description = "GEO 地理位置")
    private GeoPoint location;

    @Schema(description = "公司规模 id")
    private Integer companySizeId;
}
