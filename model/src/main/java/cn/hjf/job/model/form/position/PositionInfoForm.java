package cn.hjf.job.model.form.position;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "创建职位表单")
public class PositionInfoForm {

    @Schema(description = "职位名称")
    private String positionName;

    @Schema(description = "职位描述")
    private String positionDescription;

    @Schema(description = "职位类型 0表示全职，1表示兼职, 2表示实习")
    private Integer positionType;

    @Schema(description = "负责人id")
    private Long responsibleId;

    @Schema(description = "职位类型id")
    private Long positionTypeId;

    @Schema(description = "每天工作时间")
    private Integer dailyWorkHours;

    @Schema(description = "每周工作天数")
    private Integer weeklyWorkDays;

    @Schema(description = "经验要求  经验 0：不限 1：1~3 年 2：3~5年 3：5~7年 4：7~10年 5：10年以上")
    private Integer experienceRequirement;

    @Schema(description = "学历要求  学历 0：不限 1：初中及以下  2：中专 3：高中 4：大专 5：本科 6：硕士 7：博士")
    private Integer educationRequirement;

    @Schema(description = "最低薪资，单位为k")
    private Integer minSalary;

    @Schema(description = "最高薪资，单位为k")
    private Integer maxSalary;

    @Schema(description = "地址id")
    private Long addressId;
}
