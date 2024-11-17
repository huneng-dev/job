package cn.hjf.job.model.vo.company;

import cn.hjf.job.model.vo.position.PositionTypeVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "子行业vo类")
public class SubIndustriesVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "子行业id")
    private Long id;

    @Schema(description = "行业名称")
    private String industryName;

    @Schema(description = "职位类型")
    private List<PositionTypeVo> positionTypeVos;
}
