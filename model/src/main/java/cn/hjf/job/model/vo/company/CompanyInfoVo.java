package cn.hjf.job.model.vo.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "公司信息")
public class CompanyInfoVo {
    @Schema(description = "公司 id")
    private Long id;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "公司logo")
    private String companyLogo;

    @Schema(description = "公司规模 id")
    private Integer companySizeId;

}
