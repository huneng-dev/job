package cn.hjf.job.model.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "公司信息")
public class CompanyInfoQuery {

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "公司logo")
    private String companyLogo;

    @Schema(description = "状态")
    private Integer status;
}
