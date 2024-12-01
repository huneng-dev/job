package cn.hjf.job.model.form.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录信息表单")
public class LoginInfoForm {

    // 手机号校验：11位数字
    @Pattern(regexp = "^[0-9]{11}$", message = "手机号必须是11位数字")
    @Schema(description = "手机号")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;

    // 密码校验：8-20位，至少一个字母，一个特殊符号
    @Size(min = 8, max = 20, message = "密码长度必须在8到20位之间")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z!@#$%^&*(),.?\":{}|<>]{8,20}",
            message = "密码必须包含字母和至少一个常用特殊符号")
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;

    @Schema(description = "验证码")
    private String validateCode;

    @Schema(description = "登录方式")
    private Integer loginMethod;

    @Schema(description = "客户端类型")
    private Integer clientType;
}
