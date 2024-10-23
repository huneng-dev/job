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
@Schema(description = "UserRole")
@TableName("user_role")
public class UserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "用户id")
    @TableField("user_role")
    private Long userId;

    @Schema(description = "角色id")
    @TableField("user_role")
    private Long roleId;
    }
