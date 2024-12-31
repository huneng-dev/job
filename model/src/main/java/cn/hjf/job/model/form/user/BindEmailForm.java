package cn.hjf.job.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "绑定邮箱")
public class BindEmailForm {

    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    @Pattern(regexp = "^(?!.*\\s).*$", message = "邮箱不能包含空格")
    private String email;

    @Pattern(regexp = "^(?!.*\\s).*$", message = "验证码不能包含空格")
    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码")
    private String validateCode;
}
