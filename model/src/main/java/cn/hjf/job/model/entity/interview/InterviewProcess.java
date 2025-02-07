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
 * @date 2025-02-04
 */
@Data
@Schema(description = "InterviewProcess")
@TableName("interview_process")
public class InterviewProcess extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "关联面试记录")
    @TableField("interview_id")
    private Long interviewId;

    @Schema(description = "面试方式：0 网络，1 现场 ")
    @TableField("method")
    private Integer method;

    @Schema(description = "面试描述")
    @TableField("description")
    private String description;

    @Schema(description = "面试开始时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    @Schema(description = "预计用时（分钟）")
    @TableField("duration")
    private Integer duration;

    @Schema(description = "状态 0 计划中，1 完成，2 应聘者缺席，3 取消")
    @TableField("status")
    private Integer status;
    }
