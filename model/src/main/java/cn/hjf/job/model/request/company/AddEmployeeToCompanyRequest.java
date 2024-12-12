package cn.hjf.job.model.request.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "添加员工到公司")
public class AddEmployeeToCompanyRequest {

    @Schema(description = "公司id")
    private Long companyId;

    @Schema(description = "验证码")
    private String verificationCode;

}
