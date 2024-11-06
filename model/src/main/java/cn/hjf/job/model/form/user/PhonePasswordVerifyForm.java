package cn.hjf.job.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "手机号密码校验表单")
public class PhonePasswordVerifyForm {

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "密码")
    private String password;

}
