package cn.hjf.job.model.vo.position;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "职位类型")
public class PositionTypeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "职位类型id")
    private Long id;

    @Schema(description = "行业id")
    private Long industryId;

    @Schema(description = "类型名")
    private String typeName;

    @Schema(description = "描述")
    private String description;
}
