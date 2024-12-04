package cn.hjf.job.model.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultUserRoleRequest {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "角色")
    private List<Long> roles;

    @Schema(description = "访问密钥key")
    private String key;
}
