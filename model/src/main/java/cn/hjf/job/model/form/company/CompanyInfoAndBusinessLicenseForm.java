package cn.hjf.job.model.form.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "公司和营业执照表单")
public class CompanyInfoAndBusinessLicenseForm {

    @Schema(description = "公司信息表单")
    private CompanyInfoForm companyInfoForm;

    @Schema(description = "营业执照表单")
    private CompanyBusinessLicenseForm companyBusinessLicenseForm;
}
