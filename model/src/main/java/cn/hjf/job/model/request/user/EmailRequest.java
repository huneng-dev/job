package cn.hjf.job.model.request.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "邮箱")
public class EmailRequest {

    @Email(message = "邮箱格式不正确")
    @NotNull(message = "邮箱不能为空")
    @Schema(description = "邮箱")
    private String email;
}