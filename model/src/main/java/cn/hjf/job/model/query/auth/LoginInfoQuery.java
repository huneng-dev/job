package cn.hjf.job.model.query.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "登录信息实体")
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfoQuery {

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像url")
    private String avatar;

    @Schema(description = "JWT")
    private String token;
}
