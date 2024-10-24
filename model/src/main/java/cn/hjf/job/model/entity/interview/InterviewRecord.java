package cn.hjf.job.model.entity.interview;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
                                                                                                                                    /**
 * 
 * @author hjf
 * @date 2024-10-25
 */
@Data
@Schema(description = "InterviewRecord")
@TableName("interview_record")
public class InterviewRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "职位id")
    @TableField("position_id")
    private Long positionId;

    @Schema(description = "应聘者id")
    @TableField("candidate_id")
    private Long candidateId;

    @Schema(description = "创建人id")
    @TableField("creator_id")
    private Long creatorId;

    @Schema(description = "备注")
    @TableField("note")
    private String note;

    @Schema(description = "当前阶段")
    @TableField("current_stage")
    private Long currentStage;

    @Schema(description = "面试状态 0 表示进行中 1 表示已通过 2 未通过")
    @TableField("status")
    private Integer status;
    }
