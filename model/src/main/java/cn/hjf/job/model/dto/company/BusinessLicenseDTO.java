package cn.hjf.job.model.dto.company;

import cn.hjf.job.model.vo.company.BusinessLicenseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "营业执照")
public class BusinessLicenseDTO {

    @Schema(description = "营业执照回显 url")
    private String url;

    @Schema(description = "营业执照信息")
    private BusinessLicenseVo businessLicenseVo;
}
