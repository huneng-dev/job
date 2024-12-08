package cn.hjf.job.model.form.company;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "营业执照表单")
public class CompanyBusinessLicenseForm {

    @NotNull
    @Schema(description = "公司名称")
    private String name;

    @NotNull
    @Schema(description = "统一社会信用代码")
    private String licenseNumber;

    @Schema(description = "注册资本")
    private String capital;

    @NotNull
    @Schema(description = "法人姓名")
    private String legalPerson;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "经营范围")
    private String businessScope;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "营业期限")
    private String period;

    @Schema(description = "成立日期")
    private String establishmentDate;

    @Schema(description = "登记日期")
    private String registrationDate;

    @NotNull
    @Schema(description = "注册机构")
    private String registrationAuthority;

    @NotNull
    @Schema(description = "营业执照照片")
    private String businessLicenseUrl;
}
