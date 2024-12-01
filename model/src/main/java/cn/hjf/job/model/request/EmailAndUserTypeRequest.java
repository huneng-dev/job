package cn.hjf.job.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAndUserTypeRequest {

    @Schema(description = "邮件")
    private String email;

    @Schema(description = "用户类型")
    private Integer userType;

    @Schema(description = "访问密钥")
    private String key;
}
