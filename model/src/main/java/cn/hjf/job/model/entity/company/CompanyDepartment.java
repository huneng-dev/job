package cn.hjf.job.model.entity.company;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
                                                                                            /**
 * 
 * @author hjf
 * @date 2024-10-23
 */
@Data
@Schema(description = "CompanyDepartment")
@TableName("company_department")
public class CompanyDepartment extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "公司id")
    @TableField("company_department")
    private Long companyId;

    @Schema(description = "部门名称")
    @TableField("company_department")
    private String departmentName;

    @Schema(description = "父部门id")
    @TableField("company_department")
    private Long parentId;

    @Schema(description = "部门描述")
    @TableField("company_department")
    private String departmentDescription;
    }
