package cn.hjf.job.model.entity.recruiter;

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
@Schema(description = "UserDepartment")
@TableName("user_department")
public class UserDepartment extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "部门id")
    @TableField("user_department")
    private Long departmentId;

    @Schema(description = "用户id")
    @TableField("user_department")
    private Long userId;

    @Schema(description = "是否是部门管理员 0表示不是 1表示是")
    @TableField("user_department")
    private Integer isManager;
    }
