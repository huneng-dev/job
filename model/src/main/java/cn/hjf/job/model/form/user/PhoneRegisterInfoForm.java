package cn.hjf.job.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "手机号注册表单")
public class PhoneRegisterInfoForm {

    // 手机号校验：11位数字
    @Pattern(regexp = "^[0-9]{11}$", message = "手机号必须是11位数字")
    @Schema(description = "手机号")
    @Pattern(regexp = "^(?!.*\\s).*$", message = "手机号不能包含空格")
    private String phone;

    // 密码校验：8-20位，至少一个字母，一个特殊符号
    @Size(min = 8, max = 20, message = "密码长度必须在8到20位之间")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{8,20}$",
            message = "密码必须包含字母和至少一个常用特殊符号")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?!.*\\s).*$", message = "密码不能包含空格")
    @Schema(description = "密码")
    private String password;

    // 确认密码
    @NotBlank(message = "确认密码不能为空")
    @Pattern(regexp = "^(?!.*\\s).*$", message = "确认密码不能包含空格")
    @Schema(description = "确认密码")
    private String confirmPassword;

    @Pattern(regexp = "^(?!.*\\s).*$", message = "验证码不能包含空格")
    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码")
    private String validateCode;


    @Schema(description = "客户端类型")
    private Integer clientType;

    // 自定义校验确认密码与密码是否一致
    @AssertTrue(message = "确认密码与密码不一致")
    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }

}
