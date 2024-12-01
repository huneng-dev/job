package cn.hjf.job.model.form.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "邮件验证码登录表单")
public class EmailCodeLoginForm {

    @Schema(description = "邮件")
    @Email(message = "邮件账号错误")
    @NotBlank(message = "邮件不能为空")
    private String email;

    @Pattern(regexp = "^[0-9]{6}$", message = "验证码必须是6位数字")
    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码")
    private String validateCode;

    @Schema(description = "客户端类型")
    private Integer clientType;
}
