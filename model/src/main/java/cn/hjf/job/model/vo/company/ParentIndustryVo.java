package cn.hjf.job.model.vo.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "父行业类")
public class ParentIndustryVo {

    @Schema(description = "行业id")
    private Long id;

    @Schema(description = "行业名称")
    private String industryName;
}
