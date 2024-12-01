package cn.hjf.job.model.vo.email;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "邮件验证码配置类")
public class EmailVerificationCode implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "验证码类型，如：注册，找回密码")
    private String action;

    @Schema(description = "收件邮箱")
    private String to;
}
