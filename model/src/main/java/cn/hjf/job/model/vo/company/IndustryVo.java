package cn.hjf.job.model.vo.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "行业类")
public class IndustryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "行业id")
    private Long id;

    @Schema(description = "行业名称")
    private String industryName;

    @Schema(description = "子行业或下级行业")
    private List<SubIndustriesVo> subIndustriesVo;
}
