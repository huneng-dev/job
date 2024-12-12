package cn.hjf.job.model.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户权限")
public class UserRoleRequest {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "角色")
    private Long role;

    @Schema(description = "访问密钥key")
    private String key;
}
