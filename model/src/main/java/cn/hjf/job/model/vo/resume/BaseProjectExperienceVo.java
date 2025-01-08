package cn.hjf.job.model.vo.resume;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "基础项目描述")
public class BaseProjectExperienceVo {

    @Schema(description = "项目id")
    private Long id;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "角色")
    private String role;
}
