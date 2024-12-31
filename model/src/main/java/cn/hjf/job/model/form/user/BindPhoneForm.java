package cn.hjf.job.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "绑定手机号")
public class BindPhoneForm {

    // 手机号校验：11位数字
    @Pattern(regexp = "^[0-9]{11}$", message = "手机号必须是11位数字")
    @Schema(description = "手机号")
    @Pattern(regexp = "^(?!.*\\s).*$", message = "手机号不能包含空格")
    private String phone;

    @Pattern(regexp = "^(?!.*\\s).*$", message = "验证码不能包含空格")
    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码")
    private String validateCode;
}
