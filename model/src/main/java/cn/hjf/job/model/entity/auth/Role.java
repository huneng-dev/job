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
@Schema(description = "Role")
@TableName("role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "角色名称")
    @TableField("role")
    private String roleName;

    @Schema(description = "角色标识符")
    @TableField("role")
    private String roleKey;

    @Schema(description = "角色描述")
    @TableField("role")
    private String roleDescription;
    }
