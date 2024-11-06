package cn.hjf.job.model.entity.company;

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
@Schema(description = "CompanyDepartmentEmployee")
@TableName("company_department_employee")
public class CompanyDepartmentEmployee extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "公司id")
    @TableField("company_id")
    private Long companyId;

    @Schema(description = "部门id")
    @TableField("department_id")
    private Long departmentId;

    @Schema(description = "员工id （对应用户id）")
    @TableField("employee")
    private Long employee;
    }
