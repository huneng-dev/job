package cn.hjf.job.model.entity.company;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
                                                                                            /**
 * 
 * @author hjf
 * @date 2024-10-25
 */
@Data
@Schema(description = "CompanyDepartment")
@TableName("company_department")
public class CompanyDepartment extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "公司id")
    @TableField("company_id")
    private Long companyId;

    @Schema(description = "部门名称")
    @TableField("department_name")
    private String departmentName;

    @Schema(description = "父部门id")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "部门描述")
    @TableField("department_description")
    private String departmentDescription;
    }
