package cn.hjf.job.model.vo.position;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "招聘端基本职位信息")
public class RecruiterBasePositionInfoVo {

    @Schema(description = "职位id")
    private Long id;

    @Schema(description = "职位名称")
    private String positionName;

    @Schema(description = "职位类型id")
    private Long positionTypeId;

    @Schema(description = "学历要求  学历 0：不限|无 1：初中及以下  2：中专 3：高中 4：大专 5：本科 6：硕士 7：博士")
    private Integer educationRequirement;

    @Schema(description = "经验要求 实习为null  经验 0：不限 1：1~3 年 2：3~5年 3：5~7年 4：7~10年 5：10年以上")
    private Integer experienceRequirement;

    @Schema(description = "最低薪资，单位为k")
    private Integer minSalary;

    @Schema(description = "最高薪资，单位为k")
    private Integer maxSalary;

    @Schema(description = "状态 0 未公开 ，1 招聘中 ，2 停招")
    private Integer status;

    @Schema(description = "查看数量")
    private Long watchCount;

    @Schema(description = "交流数量")
    private Long communicationCount;

    @Schema(description = "收藏数量")
    private Long favoriteCount;
}
