package cn.hjf.job.model.entity.position;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
*
* @author hjf
* @date 2024-10-31
*/
@Data
@Schema(description = "PositionInfo")
@TableName("position_info")
public class PositionInfo extends BaseEntity {

private static final long serialVersionUID = 1L;

@Schema(description = "公司id")
@TableField("company_id")
private Long companyId;

@Schema(description = "部门id")
@TableField("department_id")
private Long departmentId;

@Schema(description = "创建人id")
@TableField("creator_id")
private Long creatorId;

@Schema(description = "负责人id")
@TableField("responsible_id")
private Long responsibleId;

@Schema(description = "地址id")
@TableField("address_id")
private Long addressId;

@Schema(description = "职位类型id")
@TableField("position_type_id")
private Long positionTypeId;

@Schema(description = "职位名称")
@TableField("position_name")
private String positionName;

@Schema(description = "职位描述 (MongoDB)")
@TableField("position_description")
private String positionDescription;

@Schema(description = "职位类型 0表示全职，1表示兼职, 2表示实习")
@TableField("position_type")
private Integer positionType;

@Schema(description = "学历要求  学历 0：不限|无 1：初中及以下  2：中专 3：高中 4：大专 5：本科 6：硕士 7：博士")
@TableField("education_requirement")
private Integer educationRequirement;

@Schema(description = "经验要求 实习为null  经验 0：不限 1：1~3 年 2：3~5年 3：5~7年 4：7~10年 5：10年以上")
@TableField("experience_requirement")
private Integer experienceRequirement;

@Schema(description = "实习时间 （实习职位才会生效） 最高12个月")
@TableField("internship_duration")
private Integer internshipDuration;

@Schema(description = "每天工作时间")
@TableField("daily_work_hours")
private Integer dailyWorkHours;

@Schema(description = "每周工作天数")
@TableField("weekly_work_days")
private Integer weeklyWorkDays;

@Schema(description = "最低薪资，单位为k")
@TableField("min_salary")
private Integer minSalary;

@Schema(description = "最高薪资，单位为k")
@TableField("max_salary")
private Integer maxSalary;

@Schema(description = "状态 0 未公开 ，1 招聘中 ，2 停招")
@TableField("status")
private Integer status;
}
