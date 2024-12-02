package cn.hjf.job.model.form.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "邮箱密码登录表单")
public class EmailPasswordLoginForm {

    @Schema(description = "邮件")
    @Email(message = "邮件账号错误")
    @NotBlank(message = "邮件不能为空")
    private String email;

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
