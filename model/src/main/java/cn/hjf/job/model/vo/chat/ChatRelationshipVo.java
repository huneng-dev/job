package cn.hjf.job.model.vo.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "聊天关系类")
public class ChatRelationshipVo {

    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "招聘者 ID")
    private Long recruiterId;

    @Schema(description = "应聘者 ID")
    private Long candidateId;

    @Schema(description = "职位 ID")
    private Long positionId;

    @Schema(description = "屏蔽状态（0: 未屏蔽, 1: 招聘者屏蔽, 2: 应聘者屏蔽, 3: 双方屏蔽）")
    private Integer blocked;

    @Schema(description = "招聘者删除（0:展示，1:不展示）")
    private Integer deletedByRecruiter;

    @Schema(description = "应聘者删除（0:展示，1:不展示）")
    private Integer deletedByCandidate;

    @Schema(description = "聊天关系类型（0: 初步面试, 1: 面试中, 2: 已聘用等）")
    private Integer relationshipType;

    @Schema(description = "更新时间")
    private Date updateTime;
}
