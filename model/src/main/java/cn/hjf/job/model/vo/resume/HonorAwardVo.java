package cn.hjf.job.model.vo.resume;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "荣誉奖励")
public class HonorAwardVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "简历id")
    private Long resumeId;

    @Schema(description = "奖项名称")
    private String awardName;
}
