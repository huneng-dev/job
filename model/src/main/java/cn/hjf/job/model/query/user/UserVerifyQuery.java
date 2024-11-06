package cn.hjf.job.model.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "校验结果")
public class UserVerifyQuery {

    @Schema(description = "用户唯一id")
    private Long id;

    @Schema(description = "用户类型")
    private Integer type;

    @Schema(description = "校验状态 状态 0：用户不存在 1：密码错误 2：用户禁用 3：密码校验成功")
    private Integer states;
}
