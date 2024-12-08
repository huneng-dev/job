package cn.hjf.job.model.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "返回公司名和id")
public class CompanyIdAndNameDTO {

    @Schema(description = "公司id")
    private Long id;

    @Schema(description = "公司名")
    private String companyName;
}
