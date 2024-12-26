package cn.hjf.job.model.vo.base;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ES分页实体")
public class PagePositionEsVo<T> implements Serializable {

    @Schema(description = "得分")
    private Double score;

    @Schema(description = "更新时间")
    private Long updateTime;

    @Schema(description = "记录条数")
    private Long total;

    @Schema(description = "数据列表")
    private List<T> records;
}
