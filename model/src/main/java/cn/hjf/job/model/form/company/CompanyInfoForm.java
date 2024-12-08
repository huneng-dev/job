package cn.hjf.job.model.form.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "公司信息表单")
public class CompanyInfoForm {

    @Schema(description = "公司名称 简称")
    private String companyName;

    @Schema(description = "公司Logo")
    private String companyLogo;

    @Schema(description = "公司规模 id")
    private Integer companySizeId;

    @Schema(description = "行业id")
    private Long industryId;

    @Schema(description = "公司描述")
    private String companyDescription;

    @Schema(description = "公司网站")
    private String companyWebsite;
}
