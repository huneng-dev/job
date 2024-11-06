package cn.hjf.job.model.form.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录信息表单")
public class LoginInfoForm {

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "验证码")
    private String validateCode;

    @Schema(description = "登录方式")
    private Integer loginMethod;

    @Schema(description = "客户端类型")
    private Integer clientType;
}
