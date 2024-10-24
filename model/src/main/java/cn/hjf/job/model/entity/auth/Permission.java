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
@Schema(description = "Permission")
@TableName("permission")
public class Permission extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "权限名")
    @TableField("permission_name")
    private String permissionName;

    @Schema(description = "权限标识符")
    @TableField("permission_key")
    private String permissionKey;

    @Schema(description = "权限描述")
    @TableField("permission_description")
    private String permissionDescription;
    }
