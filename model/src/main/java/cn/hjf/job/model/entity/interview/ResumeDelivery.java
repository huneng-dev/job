package cn.hjf.job.model.entity.interview;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
                                                                                                                /**
 * 简历投递记录表
 * @author hjf
 * @date 2025-02-04
 */
@Data
@Schema(description = "ResumeDelivery")
@TableName("resume_delivery")
public class ResumeDelivery extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "招聘者id")
    @TableField("recruiter_id")
    private Long recruiterId;

    @Schema(description = "应聘者id")
    @TableField("candidate_id")
    private Long candidateId;

    @Schema(description = "职位id")
    @TableField("position_id")
    private Long positionId;

    @Schema(description = "简历id")
    @TableField("resume_id")
    private Long resumeId;

    @Schema(description = "状态：0 未查看，1 已查看，2 拒绝，3 面试，4 录用")
    @TableField("status")
    private Integer status;
    }
