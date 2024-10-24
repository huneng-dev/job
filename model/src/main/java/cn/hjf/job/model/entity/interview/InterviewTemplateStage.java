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
@Schema(description = "InterviewTemplateStage")
@TableName("interview_template_stage")
public class InterviewTemplateStage extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "面试流程模板id")
    @TableField("interview_template_id")
    private Long interviewTemplateId;

    @Schema(description = "阶段名称")
    @TableField("stage_name")
    private String stageName;

    @Schema(description = "阶段在流程中的顺序  从0开始")
    @TableField("stage_order")
    private Integer stageOrder;
    }
