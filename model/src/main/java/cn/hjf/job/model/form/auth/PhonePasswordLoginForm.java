package cn.hjf.job.model.form.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "手机密码登录表单")
public class PhonePasswordLoginForm {

    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][3-9][0-9]{9}$", message = "手机号格式不正确，必须是11位数字")
    @Pattern(regexp = "^(?!.*\\s).*$", message = "手机号不能包含空格")
    private String phone;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度必须在8到20位之间")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{8,20}$",
            message = "密码必须包含字母和至少一个常用特殊符号")
    @Pattern(regexp = "^(?!.*\\s).*$", message = "密码不能包含空格")
    private String password;


    @Schema(description = "客户端类型")
    private Integer clientType;
}
