package cn.hjf.job.model.form.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "地址表单")
public class AddressInfoForm {
    @Schema(description = "省")
    private String province;

    @Schema(description = "市")
    private String city;

    @Schema(description = "县 | 区")
    private String district;

    @Schema(description = "详细地址")
    private String address;
}
