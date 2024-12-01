package cn.hjf.job.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "手机号")
public class PhoneRequest {

    @NotNull(message = "手机号不能位空")
    @Schema(description = "手机号")
    @Pattern(regexp = "^\\d{11}$", message = "手机号必须是 11 位数字")
    private String phone;
}
