package cn.hjf.job.model.vo.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "公司规模")
public class CompanySizeVo {

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "描述")
    private String sizeDescription;
}
