package cn.hjf.job.model.entity.interview;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * 
 * @author hjf
 * @date 2024-10-25
 */
@Data
@Schema(description = "InterviewStage")
@TableName("interview_stage")
public class InterviewStage extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "面试记录id")
    @TableField("record_id")
    private Long recordId;

    @Schema(description = "阶段id")
    @TableField("stage_id")
    private Long stageId;

    @Schema(description = "面试开始时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    @Schema(description = "面试地点")
    @TableField("interview_location")
    private String interviewLocation;

    @Schema(description = "状态 0 未开始 1 进行中 2 已完成")
    @TableField("status")
    private Integer status;
    }
