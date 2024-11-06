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
@Schema(description = "CompanySize")
@TableName("company_size")
public class CompanySize extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "公司规模描述。如：10-20人")
    @TableField("size_description")
    private String sizeDescription;

    @Schema(description = "员工最小人数")
    @TableField("min_employee_count")
    private Integer minEmployeeCount;

    @Schema(description = "员工最大人数")
    @TableField("max_employee_count")
    private Integer maxEmployeeCount;
    }
