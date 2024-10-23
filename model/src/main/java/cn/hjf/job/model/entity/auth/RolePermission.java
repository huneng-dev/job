package cn.hjf.job.model.entity.auth;

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
@Schema(description = "RolePermission")
@TableName("role_permission")
public class RolePermission extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "角色id")
    @TableField("role_permission")
    private Long roleId;

    @Schema(description = "权限id")
    @TableField("role_permission")
    private Long permissionId;
    }
