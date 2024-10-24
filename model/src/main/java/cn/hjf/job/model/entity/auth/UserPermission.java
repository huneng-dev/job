package cn.hjf.job.model.entity.auth;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
                                                                        /**
 * 
 * @author hjf
 * @date 2024-10-24
 */
@Data
@Schema(description = "UserPermission")
@TableName("user_permission")
public class UserPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "授权者id")
    @TableField("granter_id")
    private Long granterId;

    @Schema(description = "权限id")
    @TableField("permission_id")
    private Long permissionId;
    }
