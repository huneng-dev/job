package cn.hjf.job.model.entity.application;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * 
 * @author hjf
 * @date 2024-10-24
 */
@Data
@Schema(description = "ApplicationRecord")
@TableName("application_record")
public class ApplicationRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "应聘者id")
    @TableField("candidate_id")
    private Long candidateId;

    @Schema(description = "简历id")
    @TableField("resume_id")
    private Long resumeId;

    @Schema(description = "职位id")
    @TableField("position_id")
    private Long positionId;

    @Schema(description = "投递状态 0未查看 ， 1 已查看 ，2 等待面试 ，3 已拒绝")
    @TableField("status")
    private Integer status;

    @Schema(description = "申请时间")
    @TableField("application_time")
    private LocalDateTime applicationTime;

    @Schema(description = "拒绝原因")
    @TableField("rejection_reason")
    private String rejectionReason;
    }
