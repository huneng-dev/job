package cn.hjf.job.model.form.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "手机验证码登录表单")
public class PhoneCodeLoginForm {

    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][3-9][0-9]{9}$", message = "手机号格式不正确，必须是11位数字")
    @Pattern(regexp = "^(?!.*\\s).*$", message = "手机号不能包含空格")
    private String phone;

    @Pattern(regexp = "^[0-9]{6}$", message = "验证码必须是6位数字")
    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码")
    private String validateCode;

    @Schema(description = "客户端类型")
    private Integer clientType;
}
