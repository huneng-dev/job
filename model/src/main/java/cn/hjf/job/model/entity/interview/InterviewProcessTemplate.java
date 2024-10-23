package cn.hjf.job.model.entity.interview;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
                                                                        /**
 * 
 * @author hjf
 * @date 2024-10-23
 */
@Data
@Schema(description = "InterviewProcessTemplate")
@TableName("interview_process_template")
public class InterviewProcessTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "公司id  null表示系统创建")
    @TableField("interview_process_template")
    private Long companyId;

    @Schema(description = "创建人id null表示系统创建")
    @TableField("interview_process_template")
    private Long creatorId;

    @Schema(description = "模板名称")
    @TableField("interview_process_template")
    private String templateName;
    }
