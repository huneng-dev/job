package cn.hjf.job.model.vo.interview;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "简历投递信息")
public class ResumeDeliveryVo {

    @Schema(description = "Id")
    private Long id;

    @Schema(description = "招聘者id")
    private Long recruiterId;

    @Schema(description = "应聘者id")
    private Long candidateId;

    @Schema(description = "职位id")
    private Long positionId;

    @Schema(description = "简历id")
    private Long resumeId;

    @Schema(description = "状态：0 未查看，1 已查看，2 拒绝，3 面试，4 录用")
    private Integer status;
}
