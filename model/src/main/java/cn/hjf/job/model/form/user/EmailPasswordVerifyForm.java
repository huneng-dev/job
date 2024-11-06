package cn.hjf.job.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "邮箱密码校验表单")
public class EmailPasswordVerifyForm {

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "密码")
    private String password;
}
