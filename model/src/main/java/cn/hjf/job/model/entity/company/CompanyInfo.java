package cn.hjf.job.model.entity.company;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;
/**
 * 
 * @author hjf
 * @date 2024-10-31
 */
@Data
@Schema(description = "CompanyInfo")
@TableName("company_info")
public class CompanyInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "公司名称")
    @TableField("company_name")
    private String companyName;

    @Schema(description = "公司logo")
    @TableField("company_logo")
    private String companyLogo;

    @Schema(description = "公司规模_id")
    @TableField("company_size_id")
    private Integer companySizeId;

    @Schema(description = "公司行业_id")
    @TableField("industry_id")
    private Long industryId;

    @Schema(description = "公司描述 （MongoDB）")
    @TableField("company_description")
    private String companyDescription;

    @Schema(description = "公司官网")
    @TableField("company_website")
    private String companyWebsite;

    @Schema(description = "工作开始时间")
    @TableField("start_time")
    private LocalTime startTime;

    @Schema(description = "工作结束时间")
    @TableField("end_time")
    private LocalTime endTime;

    @Schema(description = "休息日id")
    @TableField("rest_days_id")
    private Integer restDaysId;

    @Schema(description = "当前系统下公司中的人数量")
    @TableField("count")
    private Integer count;

    @Schema(description = "公司状态，0 ： 禁用，1：停招 ，2：招聘中")
    @TableField("status")
    private Integer status;
    }
