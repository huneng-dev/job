package cn.hjf.job.model.entity.company;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hjf
 * @version 1.0
 * @description
 */
@Data
@Schema(description = "CompanyEmployee")
@TableName("company_employee")
@AllArgsConstructor
@NoArgsConstructor
public class CompanyEmployee extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "公司id")
    @TableField("company_id")
    private Long companyId;

    @Schema(description = "职称id")
    @TableField("title_id")
    private Long titleId;

    @Schema(description = "是否是管理员")
    @TableField("is_admin")
    private Integer isAdmin;
}
