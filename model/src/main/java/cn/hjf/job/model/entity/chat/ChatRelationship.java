package cn.hjf.job.model.entity.chat;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 聊天关系表
 *
 * @author hjf
 * @date 2025-01-23
 */
@Data
@Schema(description = "ChatRelationship")
@TableName("chat_relationship")
public class ChatRelationship extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "招聘者 ID")
    @TableField("recruiter_id")
    private Long recruiterId;

    @Schema(description = "应聘者 ID")
    @TableField("candidate_id")
    private Long candidateId;

    @Schema(description = "职位 ID")
    @TableField("position_id")
    private Long positionId;

    @Schema(description = "屏蔽状态（0: 未屏蔽, 1: 招聘者屏蔽, 2: 应聘者屏蔽, 3: 双方屏蔽）")
    @TableField("blocked")
    private Integer blocked;

    @Schema(description = "招聘者删除（0:展示，1:不展示）")
    @TableField("deleted_by_recruiter")
    private Integer deletedByRecruiter;

    @Schema(description = "应聘者删除（0:展示，1:不展示）")
    @TableField("deleted_by_candidate")
    private Integer deletedByCandidate;

    @Schema(description = "聊天关系类型（0: 初步面试, 1: 面试中, 2: 已聘用等）")
    @TableField("relationship_type")
    private Integer relationshipType;

}
