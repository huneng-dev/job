package cn.hjf.job.model.entity.resume;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author hjf
 * @date 2024-10-25
 */
@Data
@Schema(description = "HonorAward")
@TableName("honor_award")
public class HonorAward extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "简历id")
    @TableField("resume_id")
    private Long resumeId;

    @Schema(description = "奖项名称")
    @TableField("award_name")
    private String awardName;
}
