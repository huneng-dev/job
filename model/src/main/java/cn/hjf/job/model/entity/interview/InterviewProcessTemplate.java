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
@Schema(description = "InterviewProcessTemplate")
@TableName("interview_process_template")
public class InterviewProcessTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "公司id  null表示系统创建")
    @TableField("company_id")
    private Long companyId;

    @Schema(description = "创建人id null表示系统创建")
    @TableField("creator_id")
    private Long creatorId;

    @Schema(description = "模板名称")
    @TableField("template_name")
    private String templateName;
    }
